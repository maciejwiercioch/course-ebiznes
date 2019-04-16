package controllers

import javax.inject.{Inject, Singleton}
import play.api.mvc.{AbstractController, ControllerComponents}


@Singleton
class CategoryController @Inject()(cc: ControllerComponents) extends AbstractController(cc){

  def getCategoryById(id : String) = Action {
    Ok("return category, id:" + id)
  }

  def addCategory() = Action {
    Ok("add category")
  }

  def deleteCategoryById(id : String) = Action {
    Ok("delete category, id:" + id)
  }

  def updateCategoryById(id: String) = Action{
    Ok("update category, category id" + id)
  }

}
