import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory
import grpc.GrpcServer
import http.HttpServer
import service.BFS
import service.BFS.Point

import scala.concurrent.ExecutionContext

object Boot {

  def main(args: Array[String]): Unit = {
    val config = ConfigFactory.load().withFallback(ConfigFactory.defaultApplication())

    implicit val actorSystem: ActorSystem           = ActorSystem("labyrinth", config)
    implicit val executionContext: ExecutionContext = actorSystem.dispatcher

    val mat = Array(
            Array(1, 0, 1, 1, 1, 1, 0, 1, 1, 1),
            Array(1, 0, 1, 0, 1, 1, 1, 0, 1, 1),
            Array(1, 1, 1, 0, 1, 1, 0, 1, 0, 1),
            Array(0, 0, 0, 0, 1, 0, 0, 0, 0, 1),
            Array(1, 1, 1, 0, 1, 1, 1, 0, 1, 0),
            Array(1, 0, 1, 1, 1, 1, 0, 1, 0, 0),
            Array(1, 0, 0, 0, 0, 0, 0, 0, 0, 1),
            Array(1, 0, 1, 1, 1, 1, 0, 1, 1, 1),
            Array(1, 1, 0, 0, 0, 0, 1, 0, 0, 1)
          )

    println(new BFS().bfs(mat, Point(0, 0), Point(3, 4)))

    new HttpServer(config).run()
  }

}
