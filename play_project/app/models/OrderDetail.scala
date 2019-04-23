package models

import play.api.libs.json.Json

case class OrderDetail(id : Int, orderID: Int, productID: Int, quantity: Int)
object OrderDetail{
  implicit val orderDetailFormat = Json.format[OrderDetail]
}

