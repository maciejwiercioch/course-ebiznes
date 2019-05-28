package controllers
import javax.inject._
import models._
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.validation.Constraints._
import play.api.i18n._
import play.api.libs.json.Json
import play.api.mvc._
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

@Singleton
class CategoryController @Inject()(categoryRepo: CategoryRepository, cc: MessagesControllerComponents
                                  )(implicit ec: ExecutionContext)
  extends MessagesAbstractController(cc) {


  /**
    * The mapping for the category form.
    */
  val categoryForm: Form[CreateCategoryForm] = Form {
    mapping(
      "name" -> nonEmptyText
    )(CreateCategoryForm.apply)(CreateCategoryForm.unapply)
  }

  /**
    * A REST endpoint that gets all the categories as JSON.
    */
  def getCategories = Action.async { implicit request =>
    categoryRepo.list().map { category =>
      Ok(Json.toJson(category))
    }
  }

  def getCategoryById(id : String) = Action.async { implicit request =>
    categoryRepo.getById(id.toInt).map {
      category => Ok(Json.toJson(category))
    }
  }

  /**
    * The index action.
    */
  def index = Action { implicit request =>
     Ok(views.html.category(categoryForm))
  }

  def addCategory() = Action.async { implicit request =>

    categoryForm.bindFromRequest.fold(

      errorForm => {
        Future.successful(BadRequest("category form contains errors"))
      },

      category => {
        categoryRepo.create(category.name).map { category =>
          Created(Json.toJson(category))
        }
      }
    )
  }

  def deleteCategoryById(id : String) = Action {
    Ok("delete category, id:" + id)
  }

  def updateCategoryById(id: String) = Action{
    Ok("update category, category id" + id)
  }

}

case class CreateCategoryForm(name: String)
