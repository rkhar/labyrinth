package http

import akka.Done
import akka.actor.{ActorSystem, CoordinatedShutdown}
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import com.typesafe.config.Config
import org.slf4j.{Logger, LoggerFactory}

import scala.concurrent.ExecutionContext
import scala.concurrent.duration.DurationInt
import scala.util.{Failure, Success}

class HttpServer(config: Config)(
    implicit actorSystem: ActorSystem,
    executionContext: ExecutionContext
) {
  val log: Logger = LoggerFactory.getLogger(getClass.getSimpleName)

  val routes: Route = new HttpRoutes().routes

  def run(): Unit =
    Http().bindAndHandle(routes, config.getString("http.interface"), config.getInt("http.port")).onComplete {
      case Success(binding) =>
        val address = binding.localAddress
        actorSystem.log.info("arm online at http://{}:{}/", address.getHostString, address.getPort)

        CoordinatedShutdown(actorSystem)
          .addTask(CoordinatedShutdown.PhaseServiceRequestsDone, "http-graceful-terminate") { () =>
            binding.terminate(10.seconds).map { _ =>
              log.info("arm http://{}:{}/ graceful shutdown completed", address.getHostString, address.getPort)
              Done
            }
          }
      case Failure(ex) =>
        log.error("Failed to bind HTTP endpoint, terminating system", ex)
        actorSystem.terminate()
    }

}
