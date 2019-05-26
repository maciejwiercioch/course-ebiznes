package controllers
import javax.inject._
import models._
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.format.Formats._
import play.api.data.validation.Constraints._
import play.api.i18n._
import play.api.libs.json.Json
import play.api.mvc._
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}
import play.api.Logger

@Singleton
class ProductController @Inject()(productRepo: ProductRepository, categoryRepo: CategoryRepository, cc: MessagesControllerComponents
                                 )(implicit ec: ExecutionContext)
  extends MessagesAbstractController(cc) {



  /**
    * The mapping for the product form.
    */
  val productForm: Form[CreateProductForm] = Form {
    mapping(
      "name" -> nonEmptyText,
      "price"-> of(floatFormat),
      "category" -> number,
      "description" -> nonEmptyText
    )(CreateProductForm.apply)(CreateProductForm.unapply)
  }
  def index = Action.async { implicit request =>
    val categories = categoryRepo.list()
    categories.map(cat => Ok(views.html.index(productForm,cat)))
  }

  def getProducts() = Action.async {
    implicit request =>
      productRepo.list().map { product =>
        Ok(Json.toJson(product))
      }
  }

  def getProductsWithCategories() = Action.async {
    implicit request =>
      productRepo.getWithCategories().map { productWithCat =>
        Ok(Json.toJson(productWithCat))
      }
  }
  val logger: Logger = Logger(this.getClass())
  def addProduct() = Action.async { implicit request =>
    var a:Seq[Category] = Seq[Category]()
    val categories = categoryRepo.list().onComplete{
      case Success(cat) => a = cat
      case Failure(_) => print("fail")
    }

     //Bind the form first, then fold the result, passing a function to handle errors, and a function to handle succes.
    productForm.bindFromRequest.fold(
      // The error function. We return the index page with the error form, which will render the errors.
      // We also wrap the result in a successful future, since this action is synchronous, but we're required to return
      // a future because the person creation function returns a future.
      errorForm => {
        Future.successful(Ok(views.html.index(errorForm, a)))
      },
      product => {
        productRepo.create(product.name, product.price, product.categoryID, product.description).map { _ =>
          // If successful, we simply redirect to the index page.
          Redirect(routes.ProductController.index).flashing("success" -> "product created")
        }
      }
    )
}


  def getProductById(id : String) = Action {
    Ok("return product, id:" + id)
  }

  def deleteProductById(id : String) = Action {
    Ok("delete product, id:" + id)
  }

  def updateProductById(id: String) = Action{
    Ok("update product, product id" + id)
  }
}

case class CreateProductForm(name : String, price : Float, categoryID: Int, description: String)