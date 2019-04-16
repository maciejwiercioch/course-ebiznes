package controllers

import javax.inject.{Inject, Singleton}
import play.api.mvc.{AbstractController, ControllerComponents}

@Singleton
class UserController @Inject()(cc: ControllerComponents) extends AbstractController(cc){

  def getUserById(id : String ) = Action {
    Ok("return user, user id: " + id)
  }

  def addUser() = Action {
    Ok("add user")
  }

  def deleteUser(id : String ) = Action {
    Ok("delete user, user id: " + id)
  }

  def updateUserById(id: String) = Action{
    Ok("update user, user id" + id)
  }
}
