package service

import java.io.File

import com.typesafe.config.Config
import io.circe.Json
import io.circe.syntax.EncoderOps
import org.slf4j.{Logger, LoggerFactory}
import util.{ImageConverter, MazeAlgorithms}
import util.MazeAlgorithms.Point

object MazeSolutionService {

  case class FindPathForMatrixWithCoordinatesRequest(matrix: Array[Array[Int]], x: Int, y: Int, dx: Int, dy: Int)

  case class FindPathForMatrixWithoutCoordinatesRequest(matrix: Array[Array[Int]])
}

class MazeSolutionService(config: Config) {

  val path: String = config.getString("http.interface") + config.getString("http.port") + "/labyrinth/images/"

  val log: Logger = LoggerFactory.getLogger(getClass.getSimpleName)

  def findPath(file: File, optSrc: Option[Point] = None, optDest: Option[Point] = None): Json = {
    val blackAndWhiteImage = ImageConverter.convertImageToBlackAndWhite(file)
    val matrix             = ImageConverter.convertImageToMatrix(blackAndWhiteImage)
    val (src, dest)        = findSrcAndDest(matrix, optSrc, optDest)

    val solution = MazeAlgorithms.bfs(matrix, src, dest)
    log.info(s"solution: $solution")
    val finalImage = ImageConverter.drawAPath(blackAndWhiteImage, solution.getOrElse(List((0, 0))))
    file.delete()
    log.info(s"filename: ${finalImage.getName}")
    (path + finalImage.getName).asJson
  }

  def findPathForOptimizedImage(file: File): Json = {
    val blackAndWhiteImage = ImageConverter.convertImageToBlackAndWhite(file)
    val matrix             = ImageConverter.convertImageToMatrix(blackAndWhiteImage)
    val optimizedMatrix    = ImageConverter.optimizeMatrix(matrix)

    val (src, dest) =
      (Point(0, optimizedMatrix.head.indexOf(0)), Point(optimizedMatrix.length - 1, optimizedMatrix.last.indexOf(0)))

    val solution = MazeAlgorithms.bfs(optimizedMatrix, src, dest)
    log.info(s"solution: $solution")
    val finalImage = ImageConverter.drawAPath(blackAndWhiteImage, solution.getOrElse(List((0, 0))))
    file.delete()
    log.info(s"filename: ${finalImage.getName}")
    (path + finalImage.getName).asJson
  }

  def findPathForMatrix(matrix: Array[Array[Int]], optSrc: Option[Point], optDest: Option[Point]): Json = {
    val (src, dest) = findSrcAndDest(matrix, optSrc, optDest)
    val solution    = MazeAlgorithms.bfs(matrix, src, dest)
    val image       = ImageConverter.convertMatrixToImage(matrix)
    val finalImage  = ImageConverter.drawAPath(image, solution.getOrElse(List((0, 0))))
    (path + finalImage.getName).asJson
  }

  private def findSrcAndDest(matrix: Array[Array[Int]], optSrc: Option[Point], optDest: Option[Point]) =
    (optSrc, optDest) match {
      case (Some(src), Some(dest)) => (src, dest)
      case (None, None)            => (Point(0, matrix.head.indexOf(0)), Point(matrix.length - 1, matrix.last.indexOf(0)))
      case _                       => throw new ClassNotFoundException("No src and dest coordinates provided")
    }
}
