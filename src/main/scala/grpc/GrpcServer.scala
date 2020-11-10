package grpc

import akka.Done
import akka.actor.{ActorSystem, CoordinatedShutdown}
import akka.grpc.scaladsl.{ServerReflection, ServiceHandler}
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.http.scaladsl.{Http, HttpConnectionContext}
import org.slf4j.{Logger, LoggerFactory}

import scala.concurrent.duration.DurationInt
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

class GrpcServer(implicit actorSystem: ActorSystem, executionContext: ExecutionContext) {
//
//  val log: Logger = LoggerFactory.getLogger(getClass.getSimpleName)
//
//  def run(): Unit = {
//
//    // create greeterService handlers
//    val greeterService: PartialFunction[HttpRequest, Future[HttpResponse]] =
//      GreeterServiceHandler.partial(new GreeterServiceImpl())
//    val fileService: PartialFunction[HttpRequest, Future[HttpResponse]] =
//      FileServiceHandler.partial(new FileServiceImplementation())
//    val reflectionService = ServerReflection.partial(List(GreeterService, FileService))
//
//    val serviceHandler = ServiceHandler.concatOrNotFound(greeterService, fileService, reflectionService)
//
//    // bind greeterService handler servers to localhost:8080
//    Http()
//      .bindAndHandleAsync(
//        serviceHandler,
//        interface = "127.0.0.1",
//        port = 8080,
//        connectionContext = HttpConnectionContext()
//      )
//      .onComplete {
//        case Success(binding) =>
//          val address = binding.localAddress
//          log.info("labyrinth project is online at http://{}:{}/", address.getHostString, address.getPort)
//
//          CoordinatedShutdown(actorSystem)
//            .addTask(CoordinatedShutdown.PhaseServiceRequestsDone, "http-graceful-terminate") { () =>
//              binding.terminate(10.seconds).map { _ =>
//                log.info(
//                  "http://{}:{}/ graceful shutdown of labyrinth project completed",
//                  address.getHostString,
//                  address.getPort
//                )
//                Done
//              }
//            }
//
//        case Failure(exception) =>
//          log.error("Failed to bind, terminating system", exception)
//          actorSystem.terminate()
//      }
//  }

}
