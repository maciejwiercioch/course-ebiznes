package models

import javax.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

/**
  * A repository for order.
  *
  * @param dbConfigProvider The Play db config provider. Play will inject this for you.
  */
@Singleton
class OrderRepository @Inject() (dbConfigProvider: DatabaseConfigProvider, val userRepository: UserRepository)(implicit ec: ExecutionContext){
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  import userRepository.UserTable

  class OrderTable(tag: Tag) extends Table[Order](tag, "Order"){

    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def userID = column[Int]("userID")
    def address = column[String]("address")

    def user_fk = foreignKey("fk_Order_User",userID, user)(_.id)


    def * = (id, userID, address) <> ((Order.apply _).tupled, Order.unapply)
  }

  val order = TableQuery[OrderTable]
  val user = TableQuery[UserTable]

  def create(userID: Int, address: String) : Future[Order] = db.run {
    (order.map(o => (o.userID, o.address))
      returning order.map(_.id)
      into {case ((userID, address),id) => Order(id, userID, address)}
      // And finally, insert the person into the database
      ) += (userID, address)
  }

  /**
    * List all the orders in the database.
    */
  def list(): Future[Seq[Order]] = db.run {
    order.result
  }
  def getByUser(user_id: Int): Future[Seq[Order]] = db.run {
    order.filter(_.userID === user_id).result
  }
  def getByUsers(user_ids: List[Int]): Future[Seq[Order]] = db.run {
    order.filter(_.userID inSet user_ids).result
  }

}
