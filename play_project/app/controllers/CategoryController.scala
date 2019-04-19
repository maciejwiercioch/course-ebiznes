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
    categoryRepo.list().map { people =>
      Ok(Json.toJson(people))
    }
  }

  def getCategoryById(id : String) = Action {
    Ok("return category, id:" + id)
  }

  /**
    * The index action.
    */
  def index = Action { implicit request =>
    Ok(views.html.index(categoryForm))
  }

  def addCategory() = Action.async { implicit request =>
    // Bind the form first, then fold the result, passing a function to handle errors, and a function to handle succes.
    categoryForm.bindFromRequest.fold(
      // The error function. We return the index page with the error form, which will render the errors.
      // We also wrap the result in a successful future, since this action is synchronous, but we're required to return
      // a future because the person creation function returns a future.
      errorForm => {
        Future.successful(Ok(views.html.index(errorForm)))
      },
      // There were no errors in the from, so create the person.
      category => {
        categoryRepo.create(category.name).map { _ =>
          // If successful, we simply redirect to the index page.
          Redirect(routes.CategoryController.index).flashing("success" -> "category_created")
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
