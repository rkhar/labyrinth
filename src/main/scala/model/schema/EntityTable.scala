package model.schema

import slick.jdbc.H2Profile.api._

abstract class EntityTable[M](tag: Tag, name: String) extends Table[M](tag, name) {
  def id: Rep[Int]
}
