package models

import play.api.libs.json.Json

case class Product(id : Int, name : String, price : Float, categoryID: Int, description: String)
object Product{
  implicit val productFormat = Json.format[Product]
}
