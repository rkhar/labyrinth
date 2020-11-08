import java.awt.image.BufferedImage

import javax.imageio.ImageIO

object Boot extends App {

  val bufferedImage: BufferedImage = ImageIO.read(getClass.getResource("maze.jpg"))

  var array = Array.ofDim[Int](bufferedImage.getHeight(), bufferedImage.getWidth())

  for {
    h <- 0 until bufferedImage.getHeight()
    w <- 0 until bufferedImage.getWidth()
  } {
    val color = bufferedImage.getRGB(w, h)

    /*
     * Right, shift to the beginning position of each color i.e. 24 for alpha 16 for red, etc.
     * The shift right operation may impact the values of other channels,
      to avoid this, you need to perform bitwise and operation with 0Xff.
      This masks the variable leaving the last 8 bits and ignoring all the rest of the bits.
     */

    val (red, green, blue) = ((color >> 16) & 0xff, (color >> 8) & 0xff, color & 0xff)

    array(h)(w) = if ((red + green + blue) / 3 >= 128) 0 else 1
  }

  array = array
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

  println(array.map(_.mkString).mkString("\n"))

}
