import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory
import grpc.GrpcServer
import http.HttpServer
import model.schema.CustomersSchema.Customer
import org.slf4j.LoggerFactory
import repository.{CustomerRepository, SellerRepository}
import service.{CustomerService, SellerService}
import slick.jdbc.H2Profile
import slick.jdbc.H2Profile.api._

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

object Boot {

  def main(args: Array[String]): Unit = {
    val log    = LoggerFactory.getLogger("Boot")
    val config = ConfigFactory.load().withFallback(ConfigFactory.defaultApplication())

    implicit val actorSystem: ActorSystem           = ActorSystem("labyrinth", config)
    implicit val executionContext: ExecutionContext = actorSystem.dispatcher

//    val mat = Array(
//      Array(0, 1, 0, 0, 0, 0, 1, 0, 0, 0),
//      Array(0, 1, 0, 1, 0, 0, 0, 1, 0, 0),
//      Array(0, 0, 0, 1, 0, 0, 1, 0, 1, 0),
//      Array(1, 1, 1, 1, 0, 1, 1, 1, 1, 0),
//      Array(0, 0, 0, 1, 0, 0, 0, 1, 0, 1),
//      Array(0, 1, 0, 0, 0, 0, 1, 0, 1, 1),
//      Array(0, 1, 1, 1, 1, 1, 1, 1, 1, 0),
//      Array(0, 1, 0, 0, 0, 0, 1, 0, 0, 0),
//      Array(0, 0, 1, 1, 1, 1, 0, 1, 1, 0)
//    )

//    println(new BFS().bfs(mat, Point(0, 0), Point(3, 4)))

    val db: H2Profile.backend.Database = Database.forConfig("mydb")
    val customerRepository             = new CustomerRepository(db)
    customerRepository.createSchemaIfNotExists().onComplete {
      case Success(value)     => log.info(s"Successfully created a customers schema: $value")
      case Failure(exception) => log.error(s"Failed to create a schema: $exception")
    }

    val sellerRepository = new SellerRepository(db)
    sellerRepository.createSchemaIfNotExists().onComplete {
      case Success(value)     => log.info(s"Successfully created a sellers schema: $value")
      case Failure(exception) => log.error(s"Failed to create a schema: $exception")
    }

    val customerService = new CustomerService(customerRepository)
    val sellerService   = new SellerService(sellerRepository)

//    customerRepository.deleteCustomerById(1)

//    customerRepository.createCustomer(Customer(1, "Rassul", "Khar", 24, "kz", "ala")).onComplete {
//      case Success(value)     => log.info(s"value: $value")
//      case Failure(exception) => log.error(s"value: $exception")
//    }

    new GrpcServer().run(customerService, sellerService)
    new HttpServer(config).run()
  }

}
