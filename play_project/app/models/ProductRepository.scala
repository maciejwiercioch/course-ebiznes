package models

import javax.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

/**
  * A repository for product.
  *
  * @param dbConfigProvider The Play db config provider. Play will inject this for you.
  */
@Singleton
class ProductRepository @Inject() (dbConfigProvider: DatabaseConfigProvider, categoryRepository: CategoryRepository)(implicit ec: ExecutionContext){
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  import categoryRepository.CategoryTable

  private class ProductTable(tag: Tag) extends Table[Product](tag, "Product"){

    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def name = column[String]("name")
    def price = column[Float]("price")
    def categoryID = column[Int]("categoryID")
    def description = column[String]("description")

    def category_fk = foreignKey("fk_Product_Category",categoryID, cat)(_.id)


    def * = (id, name, price, categoryID, description) <> ((Product.apply _).tupled, Product.unapply)
  }

  import categoryRepository.CategoryTable
  private val product = TableQuery[ProductTable]
  private val cat = TableQuery[CategoryTable]

  def create(name: String, price: Float, categoryID: Int, description: String) : Future[Product] = db.run {
    (product.map(p => (p.name,p.price, p.categoryID, p.description))
      returning product.map(_.id)
      // And we define a transformation for the returned value, which combines our original parameters with the
      // returned id
      into {case ((name,price, categoryID, description),id) => Product(id,name, price, categoryID, description)}
      // And finally, insert the person into the database
      ) += (name, price, categoryID, description)
  }

  /**
    * List all the people in the database.
    */
  def list(): Future[Seq[Product]] = db.run {
    product.result
  }
  def getByCategory(category_id: Int): Future[Seq[Product]] = db.run {
    product.filter(_.categoryID === category_id).result
  }
  def getByCategories(category_ids: List[Int]): Future[Seq[Product]] = db.run {
    product.filter(_.categoryID inSet category_ids).result
  }

}
