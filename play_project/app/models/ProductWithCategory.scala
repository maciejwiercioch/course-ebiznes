package models

import play.api.libs.json.Json

case class ProductWithCategory(id : Int, name : String, price : Float, category: String, description: String)
object ProductWithCategory{
  implicit val productWithCategoryFormat = Json.format[ProductWithCategory]
}
