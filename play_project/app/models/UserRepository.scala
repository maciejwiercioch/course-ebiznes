package models
import javax.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}
/**
  * A repository for user.
  *
  * @param dbConfigProvider The Play db config provider. Play will inject this for you.
  */
@Singleton
class UserRepository @Inject() (dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  // We want the JdbcProfile for this provider
  protected val dbConfig = dbConfigProvider.get[JdbcProfile]
  // These imports are important, the first one brings db into scope, which will let you do the actual db operations.
  // The second one brings the Slick DSL into scope, which lets you define the table and other queries.
  import dbConfig._
  import profile.api._

  class UserTable(tag: Tag) extends Table[User](tag, "User") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def email = column[String]("email")
    def password = column[String]("password")
    def firstname = column[String]("firstname")
    def lastname = column[String]("lastname")
    def phone = column[String]("phone")
    def country = column[String]("country")
    def city = column[String]("city")
    def address = column[String]("address")


    def * = (id, email, password, firstname,
      lastname, phone, country, city, address) <> ((User.apply _).tupled, User.unapply)
  }

  val user = TableQuery[UserTable]

  def create(email: String, password:String, firstname: String, lastname: String, phone:String, country: String, city: String,
             address: String): Future[User] = db.run {
    // we're not inserting a value for the id column
    (user.map(u => (u.email, u.password, u.lastname, u.firstname, u.phone, u.country, u.city,u.address))
      // Now define it to return the id, because we want to know what id was generated for the user
      returning user.map(_.id)
      // And we define a transformation for the returned value, which combines our original parameters with the
      // returned id
      into {case ((email,password, firstname, lastname, phone, country, city, address),id)
    => User(id,email, password, firstname, lastname,phone,country,city,address)}
      // And finally, insert the user into the database
      ) += (email,password, firstname, lastname, phone, country, city, address)
  }
  
  def list(): Future[Seq[User]] = db.run {
    user.result
  }
}
