package models

import play.api.libs.json.Json

case class User(id: Int, email:String, password: String, firstname:String, lastname: String,
                phone: String, country:String, city: String, address: String)
object User {
  implicit val userFormat = Json.format[User]
}