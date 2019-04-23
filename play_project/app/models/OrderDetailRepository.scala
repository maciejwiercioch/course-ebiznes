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
class OrderDetailRepository @Inject() (dbConfigProvider: DatabaseConfigProvider,
                                       val orderRepository: OrderRepository, val productRepository: ProductRepository)(implicit ec: ExecutionContext){
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  import orderRepository.OrderTable
  import productRepository.ProductTable

  class OrderDetailTable(tag: Tag) extends Table[OrderDetail](tag, "OrderDetail"){

    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def orderID = column[Int]("orderID")
    def productID = column[Int]("productID")
    def quantity = column[Int]("quantity")

    def order_fk = foreignKey("fk_OrderDetail_Order", orderID, order)(_.id)
    def product_fk = foreignKey("fk_OrderDetail_Product", productID, product)(_.id)

    def * = (id, orderID, productID, quantity) <> ((OrderDetail.apply _).tupled, OrderDetail.unapply)
  }


  val order = TableQuery[OrderTable]
  val product = TableQuery[ProductTable]
  val orderDetail = TableQuery[OrderDetailTable]

  def create(orderID: Int, productID : Int,quantity: Int) : Future[OrderDetail] = db.run {
    (orderDetail.map(o => (o.orderID, o.productID, o.quantity))
      returning orderDetail.map(_.id)
      // And we define a transformation for the returned value, which combines our original parameters with the
      // returned id
      into {case ((orderID, productID, quantity),id) => OrderDetail(id, orderID, productID, quantity)}
      // And finally, insert the person into the database
      ) += (orderID, productID, quantity)
  }

  /**
    * List all the people in the database.
    */
  def list(): Future[Seq[OrderDetail]] = db.run {
    orderDetail.result
  }
  def getByOrder(order_id: Int): Future[Seq[OrderDetail]] = db.run {
    orderDetail.filter(_.orderID === order_id).result
  }
  def getByOrders(order_ids: List[Int]): Future[Seq[OrderDetail]] = db.run {
    orderDetail.filter(_.orderID inSet order_ids).result
  }
  def getByProduct(product_id: Int): Future[Seq[OrderDetail]] = db.run {
    orderDetail.filter(_.productID === product_id).result
  }
  def getByProducts(product_ids: List[Int]): Future[Seq[OrderDetail]] = db.run {
    orderDetail.filter(_.productID inSet product_ids).result
  }

}
