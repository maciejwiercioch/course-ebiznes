package models

import play.api.libs.json.Json

case class Order(id : Int, userID: Int, address: String)
object Order{
  implicit val orderFormat = Json.format[Order]
}
