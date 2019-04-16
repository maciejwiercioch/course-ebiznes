package controllers

import javax.inject.{Inject, Singleton}
import play.api.mvc.{AbstractController, ControllerComponents}


@Singleton
class OrderDetailController @Inject()(cc: ControllerComponents) extends AbstractController(cc){

  def getOrderDetailById(id : String) = Action {
    Ok("return order detail, id:" + id)
  }

  def addOrderDetail() = Action {
    Ok("add order detail")
  }

  def deleteOrderDetailById(id : String) = Action {
    Ok("delete order detail, id:" + id)
  }

  def updateOrderDetailById(id: String) = Action{
    Ok("update order detail, order detail id" + id)
  }

}
