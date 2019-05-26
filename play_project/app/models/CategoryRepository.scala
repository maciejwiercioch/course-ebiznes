package models
import javax.inject.{ Inject, Singleton }
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile
import scala.concurrent.{ Future, ExecutionContext }
/**
  * A repository for category.
  *
  * @param dbConfigProvider The Play db config provider. Play will inject this for you.
  */
@Singleton
class CategoryRepository @Inject() (dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  // We want the JdbcProfile for this provider
  val dbConfig = dbConfigProvider.get[JdbcProfile]
  // These imports are important, the first one brings db into scope, which will let you do the actual db operations.
  // The second one brings the Slick DSL into scope, which lets you define the table and other queries.
  import dbConfig._
  import profile.api._
  /**
    * Here we define the table. It will have a name of category
    */
  class CategoryTable(tag: Tag) extends Table[Category](tag, "Category") {

    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name")
    def * = (id, name) <> ((Category.apply _).tupled, Category.unapply)
  }
  /**
    * The starting point for all queries on the category table.
    */
  val category = TableQuery[CategoryTable]
  /**
    * Create a category with the given name.
    */
  def create(name: String): Future[Category] = db.run {
    (category.map(c => (c.name))
      returning category.map(_.id)
      into ((name, id) => Category(id, name))
      ) += (name)
  }
  /**
    * List all the categories in the database.
    */
  def list(): Future[Seq[Category]] = db.run {
    category.result
  }

  def getById(category_id: Int): Future[Seq[Category]] = db.run {
    category.filter(_.id === category_id).result
  }
}
