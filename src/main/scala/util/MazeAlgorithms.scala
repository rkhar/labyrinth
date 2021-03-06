package util

import scala.collection.mutable

object MazeAlgorithms {

  case class Point(x: Int, y: Int)

  val offsets: Vector[(Int, Int)] = Vector((0, -1), (1, 0), (0, 1), (-1, 0))

  def bfs(mat: Array[Array[Int]], src: Point, dest: Point): Option[List[(Int, Int)]] = {
    val queue = mutable.Queue[List[(Int, Int)]]()
    queue.enqueue(List(src.x -> src.y))
    var solution: Option[List[(Int, Int)]] = None
    var visited: Set[(Int, Int)]           = Set(src.x -> src.y)
    while (queue.nonEmpty && solution.isEmpty) {
      val steps @ (x, y) :: _ = queue.dequeue()
      for ((dx, dy) <- offsets) {
        val nx = x + dx
        val ny = y + dy
        if (nx >= 0 && nx < mat.length && ny >= 0 && ny < mat(nx).length && mat(nx)(ny) == 0 && !visited(nx -> ny)) {
          if (nx == dest.x && ny == dest.y) {
            solution = Some((nx -> ny) :: steps)
          } else {
            visited += nx -> ny
            queue.enqueue((nx -> ny) :: steps)
          }
        }
      }
    }
    solution
  }

  def dfs(mat: Array[Array[Int]], src: Point, dest: Point): Option[List[(Int, Int)]] = {
    val stack = mutable.Stack[List[(Int, Int)]]()
    stack.push(List(src.x -> src.y))
    var solution: Option[List[(Int, Int)]] = None
    var visited: Set[(Int, Int)]           = Set(src.x -> src.y)
    while (stack.nonEmpty && solution.isEmpty) {
      val steps @ (x, y) :: _ = stack.pop()
      for ((dx, dy) <- offsets) {
        val nx = x + dx
        val ny = y + dy
        if (nx >= 0 && nx < mat.length && ny >= 0 && ny < mat(nx).length && mat(nx)(ny) == 0 && !visited(nx -> ny)) {
          if (nx == dest.x && ny == dest.y) {
            solution = Some((nx -> ny) :: steps)
          } else {
            visited += nx -> ny
            stack.push((nx -> ny) :: steps)
          }
        }
      }
    }
    solution
  }

}
