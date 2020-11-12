package model.schema

import slick.jdbc.H2Profile.api._
import slick.lifted.ProvenShape

object SellersSchema {

  case class Seller(id: Int, firstName: String, secondName: String, age: Int, country: String, city: String)

  class Sellers(tag: Tag) extends EntityTable[Seller](tag, "SELLERS") {
    def id                      = column[Int]("SELLER_ID", O.PrimaryKey) // This is the primary key column
    def firstName               = column[String]("FIRST_NAME")
    def secondName              = column[String]("SECOND_NAME")
    def age                     = column[Int]("AGE")
    def country                 = column[String]("COUNTRY")
    def city                    = column[String]("CITY")
    def * : ProvenShape[Seller] = (id, firstName, secondName, age, country, city).mapTo[Seller]
  }
}
