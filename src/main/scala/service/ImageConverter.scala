package service

import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File

import akka.http.scaladsl.server.directives.FileInfo
import io.circe.Json
import io.circe.syntax.EncoderOps
import javax.imageio.ImageIO

object ImageConverter {

  def convertImageToMatrix(file: File): Array[Array[Int]] = {
    val bufferedImage: BufferedImage = ImageIO.read(file)
    val array: Array[Array[Int]]     = Array.ofDim[Int](bufferedImage.getHeight(), bufferedImage.getWidth())

    for {
      h <- 0 until bufferedImage.getHeight()
      w <- 0 until bufferedImage.getWidth()
    } {
      val color = bufferedImage.getRGB(w, h)

      val (red, green, blue) = ((color >> 16) & 0xff, (color >> 8) & 0xff, color & 0xff)

      array(h)(w) = if ((red + green + blue) / 3 >= 128) 0 else 1
    }

    array
  }

  def convertImageToBlackAndWhite(fileInfo: FileInfo, file: File): Json = {
    val image   = ImageIO.read(file)
    val result  = new BufferedImage(image.getWidth, image.getHeight, BufferedImage.TYPE_BYTE_BINARY)
    val graphic = result.createGraphics
    graphic.drawImage(image, 0, 0, Color.WHITE, null)
    graphic.dispose()

    val fileName         = fileInfo.fileName
    val nameAndExtension = fileInfo.fileName.split("\\.")
    val output           = new File(s"mazes/$fileName")
    ImageIO.write(result, nameAndExtension(1), output)
    ("http://localhost:8080/labyrinth/images/" + fileName).asJson
  }

  def convertMatrixToImage(coordinates: List[(Int, Int)], width: Int, height: Int) = {
    val array = Array.ofDim[Int](height, width)

  }

  def optimizeMatrix(array: Array[Array[Int]]): Array[Array[Int]] =
    array
      .foldLeft(Array.empty[Array[Int]]) {
        case (acc, arr) if acc.nonEmpty && acc.last.sameElements(arr) => acc
        case (acc, arr)                                               => acc :+ arr
      }
      .transpose
      .foldLeft(Array.empty[Array[Int]]) {
        case (acc, arr) if acc.nonEmpty && acc.last.sameElements(arr) => acc
        case (acc, arr)                                               => acc :+ arr
      }
      .transpose
}
