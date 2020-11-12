package grpc

import akka.Done
import akka.actor.{ActorSystem, CoordinatedShutdown}
import akka.grpc.scaladsl.{ServerReflection, ServiceHandler}
import akka.http.scaladsl.{Http, HttpConnectionContext}
import com.typesafe.config.Config
import labyrinth.customers.grpc.{CustomersGrpcService, CustomersGrpcServiceHandler}
import labyrinth.sellers.grpc.{SellersGrpcService, SellersGrpcServiceHandler}
import org.slf4j.{Logger, LoggerFactory}
import service.{CustomerService, SellerService}

import scala.concurrent.duration.DurationInt
import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

class GrpcServer(config: Config)(implicit actorSystem: ActorSystem, executionContext: ExecutionContext) {

  val log: Logger = LoggerFactory.getLogger(getClass.getSimpleName)

  def run(customerService: CustomerService, sellerService: SellerService): Unit = {

    val customersGrpcService =
      CustomersGrpcServiceHandler.partial(new CustomersGrps(customerService))
    val sellersGrpcService =
      SellersGrpcServiceHandler.partial(new SellersGrps(sellerService))
    val reflectionService = ServerReflection.partial(List(CustomersGrpcService, SellersGrpcService))

    val serviceHandler = ServiceHandler.concatOrNotFound(customersGrpcService, sellersGrpcService, reflectionService)

    Http()
      .bindAndHandleAsync(
        serviceHandler,
        interface = config.getString("grpc.interface"),
        port = config.getInt("grpc.port"),
        connectionContext = HttpConnectionContext()
      )
      .onComplete {
        case Success(binding) =>
          val address = binding.localAddress
          log.info("labyrinth project is online at http://{}:{}/", address.getHostString, address.getPort)

          CoordinatedShutdown(actorSystem)
            .addTask(CoordinatedShutdown.PhaseServiceRequestsDone, "http-graceful-terminate") { () =>
              binding.terminate(10.seconds).map { _ =>
                log.info(
                  "http://{}:{}/ graceful shutdown of labyrinth project completed",
                  address.getHostString,
                  address.getPort
                )
                Done
              }
            }

        case Failure(exception) =>
          log.error("Failed to bind, terminating system", exception)
          actorSystem.terminate()
      }
  }

}
