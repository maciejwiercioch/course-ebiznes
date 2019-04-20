package models

import play.api.libs.json.Json

case class Order(id : Int, userID: Int, address: String)
object Product{
  implicit val productFormat = Json.format[Product]
}
