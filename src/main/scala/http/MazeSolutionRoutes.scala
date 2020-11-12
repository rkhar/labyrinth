package http

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Directives.{complete, path, post, storeUploadedFile}
import akka.http.scaladsl.server.Route
import service.MazeSolutionService
import service.MazeSolutionService.{FindPathForMatrixWithCoordinatesRequest, FindPathForMatrixWithoutCoordinatesRequest}
import io.circe.generic.auto._
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import util.MazeAlgorithms.Point

trait MazeSolutionRoutes extends HttpUtil {

  val mazeSolutionService: MazeSolutionService

  def findPathWithCoordinates: Route =
    path("coordinates" / "solve") {
      formFields("x", "y", "dx", "dy") { (x, y, dx, dy) =>
        post {
          storeUploadedFile("file", tempDestination) {
            case (_, file) =>
              val response = mazeSolutionService.findPath(
                file,
                Some(Point(x.toInt, y.toInt)),
                Some(Point(dx.toInt, dy.toInt))
              )
              complete(response)
          }
        }
      }
    }

  def findPathWithoutCoordinates: Route =
    path("solve") {
      post {
        storeUploadedFile("file", tempDestination) {
          case (_, file) =>
            val response = mazeSolutionService.findPath(
              file
            )
            complete(response)
        }
      }
    }

  def findPathForOptimized: Route =
    path("optimized" / "solve") {
      post {
        storeUploadedFile("file", tempDestination) {
          case (_, file) =>
            val response = mazeSolutionService.findPathForOptimizedImage(
              file
            )
            complete(response)
        }
      }
    }

  def findPathForMatrixWithCoordinates: Route =
    path("matrix" / "coordinates" / "solve") {
      post {
        entity(as[FindPathForMatrixWithCoordinatesRequest]) { body =>
          val response = mazeSolutionService.findPathForMatrix(
            body.matrix,
            Some(Point(body.x, body.y)),
            Some(Point(body.dx, body.dy))
          )
          complete(response)
        }
      }
    }

  def findPathForMatrixWithoutCoordinates: Route =
    path("matrix" / "solve") {
      post {
        entity(as[FindPathForMatrixWithoutCoordinatesRequest]) { body =>
          val response = mazeSolutionService.findPathForMatrix(
            body.matrix,
            None,
            None
          )
          complete(response)
        }
      }
    }

  val mazeSolutionRoutes: Route = concat(
    findPathWithCoordinates,
    findPathWithoutCoordinates,
    findPathForOptimized,
    findPathForMatrixWithCoordinates,
    findPathForMatrixWithoutCoordinates
  )
}
