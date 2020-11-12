package http

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Directives.{path, post, storeUploadedFile}
import akka.http.scaladsl.server.Route
import service.MazeConverterService
import io.circe.generic.auto._
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import service.MazeConverterService.ConvertMatrixToImageRequest

trait MazeConverterRoutes extends HttpUtil {

  val mazeConverterService: MazeConverterService

  def convertImageToMatrix: Route =
    path("images" / "convert" / "matrix") {
      post {
        storeUploadedFile("file", tempDestination) {
          case (_, file) =>
            val response = mazeConverterService.convertImageToMatrix(file)
            complete(response)
        }
      }
    }

  def convertImageToMatrixAndOptimize: Route =
    path("images" / "convert" / "optimize" / "matrix") {
      post {
        storeUploadedFile("file", tempDestination) {
          case (_, file) =>
            val response = mazeConverterService.convertImageToMatrixAndOptimize(file)
            complete(response)
        }
      }
    }

  def convertImageToBlackAndWhite: Route =
    path("images" / "convert" / "bnw") {
      post {
        storeUploadedFile("file", tempDestination) {
          case (_, file) =>
            val response = mazeConverterService.convertImageToBlackAndWhite(file)
            complete(response)
        }
      }
    }

  def convertMatrixToImage: Route =
    path("matrix" / "convert" / "image") {
      post {
        entity(as[ConvertMatrixToImageRequest]) { body =>
          val response = mazeConverterService.convertMatrixToImage(body.matrix)
          complete(response)
        }
      }
    }

  def convertImageToOptimizedImage: Route =
    path("images" / "convert" / "optimized" / "image") {
      post {
        storeUploadedFile("file", tempDestination) {
          case (_, file) =>
            val response = mazeConverterService.convertImageToOptimizedImage(file)
            complete(response)
        }
      }
    }

  def getImage: Route = path("images" / Segment) { fileName =>
    getFromBrowseableDirectories(s"mazes/$fileName")
  }

  val mazeConverterRoutes: Route =
    concat(
      convertImageToMatrixAndOptimize,
      convertImageToBlackAndWhite,
      convertMatrixToImage,
      convertImageToMatrix,
      convertImageToOptimizedImage,
      getImage
    )

}
