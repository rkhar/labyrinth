package service

import java.io.File

import akka.http.scaladsl.server.directives.FileInfo
import io.circe.Json
import org.slf4j.{Logger, LoggerFactory}
import service.BFS.Point

class MazeService(fileInfo: FileInfo, file: File) {

  val log: Logger = LoggerFactory.getLogger(getClass.getSimpleName)

  def convertImageToMatrix(): Array[Array[Int]] = {
    val array = ImageConverter.convertImageToMatrix(file)
    log.info(s"array: \n ${array.map(_.mkString).mkString("\n")}")
    file.delete()
    array
  }

  def convertImageToMatrixAndOptimize(): Array[Array[Int]] = {
    val array = ImageConverter.optimizeMatrix(ImageConverter.convertImageToMatrix(file))
    log.info(s"array: \n ${array.map(_.mkString).mkString("\n")}")
    file.delete()
    array
  }

  def convertImageToBlackAndWhite(): Json = {
    val response = ImageConverter.convertImageToBlackAndWhite(fileInfo, file)
    file.delete()
    response
  }

  def findPath(src: Point, dest: Point): Option[List[(Int, Int)]] = {
    val solution = new BFS().bfs(ImageConverter.convertImageToMatrix(file), src, dest)
    file.delete()
    solution
  }

}
