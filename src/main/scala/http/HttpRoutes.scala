package http

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import service.{MazeConverterService, MazeSolutionService}

class HttpRoutes extends MazeConverterRoutes with MazeSolutionRoutes {

  override val mazeSolutionService: MazeSolutionService   = new MazeSolutionService()
  override val mazeConverterService: MazeConverterService = new MazeConverterService()

  val routes: Route = pathPrefix("labyrinth") {
    concat(mazeConverterRoutes, mazeSolutionRoutes, healthCheck)
  }

  def healthCheck: Route = path("healthcheck") {
    get {
      complete("ok")
    }
  }

}
