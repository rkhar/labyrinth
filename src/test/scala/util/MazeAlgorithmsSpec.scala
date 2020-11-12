package util

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must.Matchers
import util.MazeAlgorithms.Point

class MazeAlgorithmsSpec extends AnyFlatSpec with Matchers {

  val maze: Array[Array[Int]] = Array(
    Array(0, 1, 0, 0, 0, 0, 1, 0, 0, 0),
    Array(0, 1, 0, 1, 0, 0, 0, 1, 0, 0),
    Array(0, 0, 0, 1, 0, 0, 1, 0, 1, 0),
    Array(1, 1, 1, 1, 0, 1, 1, 1, 1, 0),
    Array(0, 0, 0, 1, 0, 0, 0, 1, 0, 1),
    Array(0, 1, 0, 0, 0, 0, 1, 0, 1, 1),
    Array(0, 1, 1, 1, 1, 1, 1, 1, 1, 0),
    Array(0, 1, 0, 0, 0, 0, 1, 0, 0, 0),
    Array(0, 0, 1, 1, 1, 1, 0, 1, 1, 0)
  )

  "A bfs algorithm" must "return list of coordinates(path) if maze is correct" in {
    val (src, dest) = (Point(0, 0), Point(8, 0))
    val result      = MazeAlgorithms.bfs(maze, src, dest)
    result mustBe Some(
      List(
        (8, 0),
        (7, 0),
        (6, 0),
        (5, 0),
        (4, 0),
        (4, 1),
        (4, 2),
        (5, 2),
        (5, 3),
        (5, 4),
        (4, 4),
        (3, 4),
        (2, 4),
        (1, 4),
        (0, 4),
        (0, 3),
        (0, 2),
        (1, 2),
        (2, 2),
        (2, 1),
        (2, 0),
        (1, 0),
        (0, 0)
      )
    )
  }

  "A bfs algorithm" must "return none if maze is incorrect" in {
    val (src, dest) = (Point(0, 0), Point(0, 1))
    val result      = MazeAlgorithms.bfs(maze, src, dest)
    result mustBe None
  }

  "A dfs algorithm" must "return list of coordinates(path) if maze is correct" in {
    val (src, dest) = (Point(0, 0), Point(8, 0))
    val result      = MazeAlgorithms.dfs(maze, src, dest)
    result mustBe Some(
      List(
        (8, 0),
        (7, 0),
        (6, 0),
        (5, 0),
        (4, 0),
        (4, 1),
        (4, 2),
        (5, 2),
        (5, 3),
        (5, 4),
        (4, 4),
        (3, 4),
        (2, 4),
        (2, 5),
        (1, 5),
        (0, 5),
        (0, 4),
        (0, 3),
        (0, 2),
        (1, 2),
        (2, 2),
        (2, 1),
        (2, 0),
        (1, 0),
        (0, 0)
      )
    )
  }

  "A dfs algorithm" must "return none if maze is incorrect" in {
    val (src, dest) = (Point(0, 0), Point(0, 1))
    val result      = MazeAlgorithms.dfs(maze, src, dest)
    result mustBe None
  }

}
