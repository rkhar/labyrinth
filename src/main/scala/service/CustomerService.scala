package service

import akka.http.scaladsl.model.StatusCodes
import model.CommonHttpResponse
import model.schema.CustomersSchema.Customer
import org.slf4j.{Logger, LoggerFactory}
import repository.CustomerRepository

import scala.concurrent.{ExecutionContext, Future}

class CustomerService(customerRepository: CustomerRepository)(implicit executionContext: ExecutionContext) {
  val log: Logger = LoggerFactory.getLogger(getClass.getSimpleName)

  def createCustomer(customer: Customer): Future[CommonHttpResponse] =
    customerRepository
      .createCustomer(customer)
      .map {
        case 1 =>
          log.info(s"Customer with customerId: ${customer.id} successfully created")
          CommonHttpResponse(
            StatusCodes.Created.intValue,
            s"Customer with customerId: ${customer.id} successfully created"
          )
        case num =>
          log.error(s"Failed to create customer with customerId: ${customer.id}, result: $num")
          CommonHttpResponse(
            StatusCodes.InternalServerError.intValue,
            s"Failed to create customer with customerId: ${customer.id}"
          )
      }
      .recover {
        case e: Throwable =>
          log.error(s"Unexpected behavior exception: $e")
          CommonHttpResponse(StatusCodes.InternalServerError.intValue, s"Unexpected behavior")
      }

  def getCustomerById(customerId: Int): Future[CommonHttpResponse] =
    customerRepository
      .getCustomerById(customerId)
      .map {
        case Some(_) =>
          log.info(s"Customer with customerId: $customerId successfully gotten")
          CommonHttpResponse(StatusCodes.OK.intValue, s"Customer with customerId: $customerId successfully gotten")
        case None =>
          log.error(s"Customer with customerId: $customerId doesn't exist")
          CommonHttpResponse(StatusCodes.NotFound.intValue, s"Customer with customerId: $customerId doesn't exist")
      }
      .recover {
        case e: Throwable =>
          log.error(s"Failed to get customer with customerId: $customerId, exception: $e")
          CommonHttpResponse(
            StatusCodes.InternalServerError.intValue,
            s"Failed to create customer with customerId: $customerId, exception: $e"
          )
      }

  def deleteCustomerById(customerId: Int): Future[CommonHttpResponse] =
    customerRepository
      .deleteCustomerById(customerId)
      .map {
        case 1 =>
          log.info(s"Customer with customerId: $customerId successfully deleted")
          CommonHttpResponse(StatusCodes.OK.intValue, s"Customer with customerId: $customerId successfully deleted")
        case num =>
          log.error(s"Failed to delete customer with customerId: $customerId, result: $num")
          CommonHttpResponse(
            StatusCodes.InternalServerError.intValue,
            s"Failed to create customer with customerId: $customerId"
          )
      }
      .recover {
        case e: Throwable =>
          log.error(s"Unexpected behavior exception: $e")
          CommonHttpResponse(StatusCodes.InternalServerError.intValue, s"Unexpected behavior")
      }
}
