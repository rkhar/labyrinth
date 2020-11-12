package repository

import model.schema.EntityTable
import slick.dbio.Effect
import slick.jdbc.H2Profile.api._
import slick.lifted.CompiledFunction
import slick.sql.{FixedSqlAction, SqlAction}

import scala.concurrent.Future

abstract class AbstractRepository[A, T <: EntityTable[A]](construct: Tag => T) extends TableQuery[T](construct) {

  val tableQuery = this

  def createSchemaIfNotExists(): Future[Unit]

  def insertValue(value: A): FixedSqlAction[Int, NoStream, Effect.Write] = this += value

  def findValueById(id: Int): SqlAction[Option[A], NoStream, Effect.Read] =
    this.filter(_.id === id).result.headOption

  def removeById(id: Int): FixedSqlAction[Int, NoStream, Effect.Write] = this.filter(_.id === id).delete
}
