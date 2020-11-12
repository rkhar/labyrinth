package repository

import model.schema.CustomersSchema.{Customer, Customers}
import slick.jdbc.H2Profile
import slick.jdbc.H2Profile.api._

import scala.concurrent.Future

class CustomerRepository(db: H2Profile.backend.Database)
    extends AbstractRepository[Customer, Customers](new Customers(_)) {

  override def createSchemaIfNotExists(): Future[Unit] = db.run(tableQuery.schema.create)

  def createCustomer(customer: Customer): Future[Int] = db.run(insertValue(customer))

  def getCustomerById(customerId: Int): Future[Option[Customer]] = db.run(findValueById(customerId))

  def deleteCustomerById(customerId: Int): Future[Int] = db.run(removeById(customerId))

}
