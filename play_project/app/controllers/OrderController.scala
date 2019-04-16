package controllers

import javax.inject.{Inject, Singleton}
import play.api.mvc.{AbstractController, ControllerComponents}

@Singleton
class OrderController @Inject()(cc: ControllerComponents) extends AbstractController(cc){

  def getOrderById(id : String) = Action {
    Ok("return order, id:" + id)
  }

  def addOrder() = Action {
    Ok("add order")
  }

  def deleteOrderById(id : String) = Action {
    Ok("delete order, id:" + id)
  }

  def updateOrderById(id: String) = Action{
    Ok("update order, order id" + id)
  }
}
