package grpc

import labyrinth.sellers.grpc.{
  CommonResponse,
  CreateSellerRequest,
  DeleteSellerByIdRequest,
  GetSellerByIdRequest,
  SellersGrpcService
}
import model.schema.SellersSchema.Seller
import org.slf4j.{Logger, LoggerFactory}
import service.SellerService

import scala.concurrent.{ExecutionContext, Future}

class SellersGrps(sellerService: SellerService)(implicit executionContext: ExecutionContext)
    extends SellersGrpcService {

  val log: Logger = LoggerFactory.getLogger(getClass.getSimpleName)

  override def createSeller(in: CreateSellerRequest): Future[CommonResponse] = {
    log.info(s"Received createSeller with sellerId: ${in.id} grpc request")
    sellerService
      .createSeller(Seller(in.id, in.firstName, in.secondName, in.age, in.country, in.city))
      .map(res => CommonResponse(res.status, res.message))
  }

  override def getSellerById(in: GetSellerByIdRequest): Future[CommonResponse] = {
    log.info(s"Received getSellerById with sellerId: ${in.id} grpc request")
    sellerService
      .getSellerById(in.id)
      .map(res => CommonResponse(res.status, res.message))
  }

  override def deleteSellerById(in: DeleteSellerByIdRequest): Future[CommonResponse] = {
    log.info(s"Received deleteSellerById with sellerId: ${in.id} grpc request")
    sellerService
      .deleteSellerById(in.id)
      .map(res => CommonResponse(res.status, res.message))
  }
}
