package grpc

import labyrinth.customers.grpc.{
  CommonResponse,
  CreateCustomerRequest,
  CustomersGrpcService,
  DeleteCustomerByIdRequest,
  GetCustomerByIdRequest
}
import model.schema.CustomersSchema.Customer
import org.slf4j.{Logger, LoggerFactory}
import service.CustomerService

import scala.concurrent.{ExecutionContext, Future}

class CustomersGrps(customerService: CustomerService)(implicit executionContext: ExecutionContext)
    extends CustomersGrpcService {

  val log: Logger = LoggerFactory.getLogger(getClass.getSimpleName)

  override def createCustomer(in: CreateCustomerRequest): Future[CommonResponse] = {
    log.info(s"Received createCustomer with customerId: ${in.id} grpc request")
    customerService
      .createCustomer(Customer(in.id, in.firstName, in.secondName, in.age, in.country, in.city))
      .map(res => CommonResponse(res.status, res.message))
  }

  override def getCustomerById(in: GetCustomerByIdRequest): Future[CommonResponse] = {
    log.info(s"Received getCustomerById with customerId: ${in.id} grpc request")
    customerService
      .getCustomerById(in.id)
      .map(res => CommonResponse(res.status, res.message))
  }

  override def deleteCustomerById(in: DeleteCustomerByIdRequest): Future[CommonResponse] = {
    log.info(s"Received deleteCustomerById with customerId: ${in.id} grpc request")
    customerService
      .deleteCustomerById(in.id)
      .map(res => CommonResponse(res.status, res.message))
  }
}
