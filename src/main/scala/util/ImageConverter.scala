package util

import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

import scala.util.Random

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

  def convertImageToBlackAndWhite(file: File): File = {
    val image   = ImageIO.read(file)
    val result  = new BufferedImage(image.getWidth, image.getHeight, BufferedImage.TYPE_BYTE_BINARY)
    val graphic = result.createGraphics
    graphic.drawImage(image, 0, 0, Color.WHITE, null)
    graphic.dispose()

    val fileName         = file.getName
    val nameAndExtension = fileName.split("\\.")
    val output           = new File(s"mazes/${nameAndExtension(0)}.jpeg")
    ImageIO.write(result, "jpeg", output)
    output
  }

  def convertMatrixToImage(matrix: Array[Array[Int]]): File = {
    val height = matrix.length
    val width  = matrix(0).length

    val image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
    for {
      x <- 0 until width
      y <- 0 until height
    } {
      println(s"x: $x, y: $y")
      println(s"${matrix(y)(x)}")

      val color =
        if (matrix(y)(x) == 1) Color.BLACK
        else Color.WHITE
      image.setRGB(x, y, color.getRGB)
    }
    val fileName = Random.alphanumeric.take(10).mkString

    val file = new File(s"mazes/$fileName.jpeg")
    ImageIO.write(image, "jpeg", file)
    file
  }

  def drawAPath(file: File, coordinates: List[(Int, Int)]): File = {
    val image    = ImageIO.read(file)
    val fileName = file.getName.split("\\.")
    coordinates.foreach(pair => image.setRGB(pair._2, pair._1, Color.RED.getRGB))
    val newFile = new File(s"mazes/${fileName(0)}.jpeg")
    ImageIO.write(image, "jpeg", newFile)
    newFile
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
