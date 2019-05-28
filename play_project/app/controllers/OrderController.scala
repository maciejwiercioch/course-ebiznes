package controllers
import javax.inject._
import models._
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json.Json
import play.api.mvc._
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

@Singleton
class OrderController @Inject()(orderRepo: OrderRepository, userRepo: UserRepository, cc: MessagesControllerComponents
                                 )(implicit ec: ExecutionContext)
  extends MessagesAbstractController(cc) {



  /**
    * The mapping for the product form.
    */
  val orderForm: Form[CreateOrderForm] = Form {
    mapping(
      "user" -> number,
      "address" -> nonEmptyText
    )(CreateOrderForm.apply)(CreateOrderForm.unapply)
  }

  def index = Action.async { implicit request =>
    val users = userRepo.list()
    users.map(user => Ok(views.html.order(orderForm,user)))
  }

  def getOrders() = Action.async {
    implicit request =>
      orderRepo.list().map { order =>
        Ok(Json.toJson(order))
      }
  }

  def addOrder() = Action.async { implicit request =>

    var a:Seq[User] = Seq[User]()
    val users = userRepo.list().onComplete{
      case Success(user) => a= user
      case Failure(_) => print("fail")
    }

    orderForm.bindFromRequest.fold(

      errorForm => {
        Future.successful(BadRequest("product form contains errors"))
      },

      order => {
        orderRepo.create(order.userID, order.address).map { order =>
          Created(Json.toJson(order))
        }
      }
    )
  }


  def getOrderById(id : String) = Action {
    Ok("return order, id:" + id)
  }

  def deleteOrderById(id : String) = Action {
    Ok("delete order, id:" + id)
  }

  def updateOrderById(id: String) = Action{
    Ok("update order, product id" + id)
  }
}

case class CreateOrderForm(userID: Int, address: String)