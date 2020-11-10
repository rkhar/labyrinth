package http

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route

class HttpRoutes extends MazeRoutes {

  val routes: Route = pathPrefix("labyrinth") {
    concat(convertImageToMatrixAndOptimize, convertImageToBlackAndWhite, getImage, healthCheck)
  }

  def healthCheck: Route = path("healthcheck") {
    get {
      complete("ok")
    }
  }
}
