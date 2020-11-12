package http

import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives.complete
import akka.http.scaladsl.server._
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import io.circe.generic.auto._
import model.CommonHttpResponse

trait CustomRejectionHandlers {

  implicit def customExceptionHandler: ExceptionHandler = ExceptionHandler {

    case ex: Throwable =>
      complete(CommonHttpResponse(StatusCodes.InternalServerError.intValue, ex.getMessage))

  }

}
