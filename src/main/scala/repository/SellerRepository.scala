package repository

import model.schema.SellersSchema.{Seller, Sellers}
import slick.jdbc.H2Profile
import slick.jdbc.H2Profile.api._

import scala.concurrent.Future

class SellerRepository(db: H2Profile.backend.Database) extends AbstractRepository[Seller, Sellers](new Sellers(_)) {

  override def createSchemaIfNotExists(): Future[Unit] = db.run(tableQuery.schema.create)

  def createSeller(seller: Seller): Future[Int] = db.run(insertValue(seller))

  def getSellerById(sellerId: Int): Future[Option[Seller]] = db.run(findValueById(sellerId))

  def deleteSellerById(sellerId: Int): Future[Int] = db.run(removeById(sellerId))
}
