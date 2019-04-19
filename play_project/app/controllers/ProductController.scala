package controllers

import javax.inject.{Inject, Singleton}
import play.api.mvc.{AbstractController, AnyContent, ControllerComponents, Request}

@Singleton
class ProductController @Inject()(cc: ControllerComponents) extends AbstractController(cc){

  def getProductById(id : String) = Action {
    Ok("return product, id:" + id)
  }

  def addProduct() = Action {
    Ok("add product")
  }

  def deleteProductById(id : String) = Action {
    Ok("delete product, id:" + id)
  }

  def updateProductById(id: String) = Action{
    Ok("update product, product id" + id)
  }
}
