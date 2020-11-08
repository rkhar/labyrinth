val array = Array.ofDim[Int](4, 2)

array(1)(0) = 2

array.foldLeft(Array.empty[Array[Int]]) {
    case (acc, arr) if acc.nonEmpty && (acc.last.sameElements(arr)) => acc
    case (acc, arr)                                                 => acc :+ arr
  }

for (i <- 0 until 5) {
    println(i)
  }