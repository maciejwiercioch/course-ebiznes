package controllers

import javax.inject.{Inject, Singleton}
import models.UserRepository
import play.api.data.Form
import play.api.data.Forms.{mapping, of}
import play.api.data.Forms._
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, ControllerComponents, MessagesAbstractController, MessagesControllerComponents}

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class UserController @Inject()(userRepo: UserRepository, cc: MessagesControllerComponents)(implicit ec: ExecutionContext)
  extends MessagesAbstractController(cc) {

  val userForm: Form[CreateUserForm] = Form {
    mapping(
      "email" -> nonEmptyText,
      "password"-> nonEmptyText,
      "lastname" -> nonEmptyText,
      "firstname" -> nonEmptyText,
      "phone"-> nonEmptyText,
      "country" -> nonEmptyText,
      "city" -> nonEmptyText,
      "address"-> nonEmptyText
    )(CreateUserForm.apply)(CreateUserForm.unapply)
  }


  def index = Action { implicit request =>
    Ok(views.html.user(userForm))
  }

  def getUsers() = Action.async {
    implicit request =>
      userRepo.list().map { user =>
        Ok(Json.toJson(user))
      }
  }

  def addUser() = Action.async { implicit request =>
    // Bind the form first, then fold the result, passing a function to handle errors, and a function to handle succes.
    userForm.bindFromRequest.fold(
      // The error function. We return the index page with the error form, which will render the errors.
      // We also wrap the result in a successful future, since this action is synchronous, but we're required to return
      // a future because the person creation function returns a future.
      errorForm => {
        Future.successful(Ok("index user"))
      },
      // There were no errors in the from, so create the person.
      user => {
        userRepo.create(user.email, user.password, user.lastname, user.firstname, user.phone, user.city, user.country, user.address).map { _ =>
          // If successful, we simply redirect to the index page.
          Redirect(routes.ProductController.index).flashing("success" -> "user_created")
        }
      }
    )
  }


  def getUserById(id : String ) = Action {
    Ok("return user, user id: " + id)
  }

  def deleteUser(id : String ) = Action {
    Ok("delete user, user id: " + id)
  }

  def updateUserById(id: String) = Action{
    Ok("update user, user id" + id)
  }
}


case class CreateUserForm(email:String, password: String, lastname:String, firstname: String,
                          phone: String, country:String, city: String, address: String)