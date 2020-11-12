package http

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.typesafe.config.Config
import service.{MazeConverterService, MazeSolutionService}

class HttpRoutes(config: Config) extends MazeConverterRoutes with MazeSolutionRoutes {

  override val mazeSolutionService: MazeSolutionService   = new MazeSolutionService(config)
  override val mazeConverterService: MazeConverterService = new MazeConverterService(config)

  val routes: Route = pathPrefix("labyrinth") {
    concat(mazeConverterRoutes, mazeSolutionRoutes, healthCheck)
  }

  def healthCheck: Route = path("healthcheck") {
    get {
      complete("ok")
    }
  }

}
