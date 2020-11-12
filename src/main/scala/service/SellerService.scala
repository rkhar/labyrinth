package service

import akka.http.scaladsl.model.StatusCodes
import model.CommonHttpResponse
import model.schema.SellersSchema.Seller
import org.slf4j.{Logger, LoggerFactory}
import repository.SellerRepository

import scala.concurrent.{ExecutionContext, Future}

class SellerService(sellerRepository: SellerRepository)(implicit executionContext: ExecutionContext) {

  val log: Logger = LoggerFactory.getLogger(getClass.getSimpleName)

  def createSeller(seller: Seller): Future[CommonHttpResponse] =
    sellerRepository
      .createSeller(seller)
      .map {
        case 1 =>
          log.info(s"Seller with sellerId: ${seller.id} successfully created")
          CommonHttpResponse(StatusCodes.Created.intValue, s"Seller with sellerId: ${seller.id} successfully created")
        case num =>
          log.error(s"Failed to create seller with sellerId: ${seller.id}, result: $num")
          CommonHttpResponse(
            StatusCodes.InternalServerError.intValue,
            s"Failed to create seller with sellerId: ${seller.id}"
          )
      }
      .recover {
        case e: Throwable =>
          log.error(s"Unexpected behavior exception: $e")
          CommonHttpResponse(StatusCodes.InternalServerError.intValue, s"Unexpected behavior")
      }

  def getSellerById(sellerId: Int): Future[CommonHttpResponse] =
    sellerRepository
      .getSellerById(sellerId)
      .map {
        case Some(_) =>
          log.info(s"Seller with sellerId: $sellerId successfully gotten")
          CommonHttpResponse(StatusCodes.OK.intValue, s"Seller with sellerId: $sellerId successfully gotten")
        case None =>
          log.error(s"Seller with sellerId: $sellerId doesn't exist")
          CommonHttpResponse(StatusCodes.NotFound.intValue, s"Seller with sellerId: $sellerId doesn't exist")
      }
      .recover {
        case e: Throwable =>
          log.error(s"Failed to get seller with sellerId: $sellerId, exception: $e")
          CommonHttpResponse(
            StatusCodes.InternalServerError.intValue,
            s"Failed to create seller with sellerId: $sellerId, exception: $e"
          )
      }

  def deleteSellerById(sellerId: Int): Future[CommonHttpResponse] =
    sellerRepository
      .deleteSellerById(sellerId)
      .map {
        case 1 =>
          log.info(s"Seller with sellerId: $sellerId successfully deleted")
          CommonHttpResponse(StatusCodes.OK.intValue, s"Seller with sellerId: $sellerId successfully deleted")
        case num =>
          log.error(s"Failed to delete seller with sellerId: $sellerId, result: $num")
          CommonHttpResponse(
            StatusCodes.InternalServerError.intValue,
            s"Failed to create seller with sellerId: $sellerId"
          )
      }
      .recover {
        case e: Throwable =>
          log.error(s"Unexpected behavior exception: $e")
          CommonHttpResponse(StatusCodes.InternalServerError.intValue, s"Unexpected behavior")
      }
}
