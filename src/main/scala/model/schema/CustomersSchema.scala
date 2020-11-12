package model.schema

import slick.jdbc.H2Profile.api._
import slick.lifted.ProvenShape

object CustomersSchema {

  case class Customer(id: Int, firstName: String, secondName: String, age: Int, country: String, city: String)

  class Customers(tag: Tag) extends EntityTable[Customer](tag, "CUSTOMERS") {
    def id                        = column[Int]("CUSTOMER_ID", O.PrimaryKey) // This is the primary key column
    def firstName                 = column[String]("FIRST_NAME")
    def secondName                = column[String]("SECOND_NAME")
    def age                       = column[Int]("AGE")
    def country                   = column[String]("COUNTRY")
    def city                      = column[String]("CITY")
    def * : ProvenShape[Customer] = (id, firstName, secondName, age, country, city).mapTo[Customer]
  }

}
