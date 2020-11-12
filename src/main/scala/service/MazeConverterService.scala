package service

import java.io.File

import io.circe.Json
import io.circe.syntax.EncoderOps
import org.slf4j.{Logger, LoggerFactory}
import util.ImageConverter

object MazeConverterService {
  case class ConvertMatrixToImageRequest(matrix: Array[Array[Int]])
  case class ConvertImageToMatrixResponse(status: String = "ok", matrix: Array[Array[Int]])
}

class MazeConverterService() {

  val log: Logger = LoggerFactory.getLogger(getClass.getSimpleName)

  def convertImageToMatrix(file: File): Array[Array[Int]] = {
    val array = ImageConverter.convertImageToMatrix(file)
    log.info(s"array: \n ${array.map(_.mkString).mkString("\n")}")
    file.delete()
    array
  }

  def convertImageToMatrixAndOptimize(file: File): Array[Array[Int]] = {
    val array = ImageConverter.optimizeMatrix(ImageConverter.convertImageToMatrix(file))
    log.info(s"array: \n ${array.map(_.mkString).mkString("\n")}")
    file.delete()
    array
  }

  def convertImageToBlackAndWhite(file: File): Json = {
    val convertedImage = ImageConverter.convertImageToBlackAndWhite(file)
    file.delete()
    ("http://localhost:8080/labyrinth/images/" + convertedImage.getName).asJson
  }

  def convertImageToOptimizedImage(file: File): Json = {
    val blackAndWhiteImage = ImageConverter.convertImageToBlackAndWhite(file)
    val matrix             = ImageConverter.convertImageToMatrix(blackAndWhiteImage)
    val optimizedMatrix    = ImageConverter.optimizeMatrix(matrix)
    val optimizedImage     = ImageConverter.convertMatrixToImage(optimizedMatrix)
    file.delete()
    ("http://localhost:8080/labyrinth/images/" + optimizedImage.getName).asJson
  }

  def drawPath(file: File, coordinates: List[(Int, Int)]): Json = {
    val newImage = ImageConverter.drawAPath(file, coordinates)
    file.delete()
    ("http://localhost:8080/labyrinth/images/" + newImage.getName).asJson
  }

  def convertMatrixToImage(matrix: Array[Array[Int]]): Json = {
    val file     = ImageConverter.convertMatrixToImage(matrix)
    val fileName = file.getName.split("\\.")(0)
    ("http://localhost:8080/labyrinth/images/" + fileName + ".jpeg").asJson
  }
}
