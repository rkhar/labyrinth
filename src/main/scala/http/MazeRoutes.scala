package http

import java.io.File

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Directives.{path, post, storeUploadedFile}
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.FileInfo
import service.MazeService
import io.circe.generic.auto._
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._

trait MazeRoutes {

  def convertImageToMatrixAndOptimize: Route =
    path("images" / "convert" / "matrix") {
      post {
        storeUploadedFile("file", tempDestination) {
          case (fileInfo, file) =>
            val mazeService = new MazeService(fileInfo, file)
            val response    = mazeService.convertImageToMatrixAndOptimize()
            complete(OK, response.map(_.mkString).mkString("\n"))
        }
      }
    }

  def convertImageToBlackAndWhite: Route =
    path("images" / "convert" / "bnw") {
      post {
        storeUploadedFile("file", tempDestination) {
          case (fileInfo, file) =>
            val mazeService = new MazeService(fileInfo, file)
            val response    = mazeService.convertImageToBlackAndWhite()
            complete(response)
        }
      }
    }

  def getImage: Route = path("images" / Segment) { fileName =>
    getFromBrowseableDirectories(s"mazes/$fileName")
  }

  def findPath: Route =
    path("path") {
      post {
        storeUploadedFile("file", tempDestination) {
          case (fileInfo, file) =>
            val mazeService = new MazeService(fileInfo, file)
            val response    = mazeService.convertImageToMatrixAndOptimize()
            complete(OK, response.map(_.mkString).mkString("\n"))
        }
      }
    }

  def tempDestination(fileInfo: FileInfo): File =
    File.createTempFile(fileInfo.fileName, ".tmp")

}
