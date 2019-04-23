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

@Singleton
class OrderDetailController @Inject()(orderDetailRepository: OrderDetailRepository,
                                      orderRepository: OrderRepository, productRepository: ProductRepository, cc: MessagesControllerComponents
                               )(implicit ec: ExecutionContext)
  extends MessagesAbstractController(cc) {



  /**
    * The mapping for the product form.
    */
  val orderDetailForm: Form[CreateOrderDetailForm] = Form {
    mapping(
      "order" -> number,
      "product" -> number,
      "quantity" -> number
    )(CreateOrderDetailForm.apply)(CreateOrderDetailForm.unapply)
  }

  def index = Action.async { implicit request =>

    val orders = orderRepository.list()
    val products = productRepository.list()
    orders.zip(products).map(element => Ok(views.html.orderdetail(orderDetailForm, element._1, element._2)))
  }

  def getOrderDetails() = Action.async {
    implicit request =>
      orderDetailRepository.list().map { order =>
        Ok(Json.toJson(order))
      }
  }

  def addOrderDetail() = Action.async { implicit request =>
    var a:Seq[Order] = Seq[Order]()
    var b:Seq[Product] = Seq[Product]()
    val orders = orderRepository.list().onComplete{
      case Success(order) => a = order
      case Failure(_) => print("fail")
    }

    val products = productRepository.list().onComplete{
      case Success(prod) => b = prod
      case Failure(_) => print("fail")
    }


    orderDetailForm.bindFromRequest.fold(

      errorForm => {
        Future.successful(Ok(views.html.orderdetail(errorForm, a, b)))
      },

      orderDetail => {
        orderDetailRepository.create(orderDetail.orderID, orderDetail.productID, orderDetail.quantity).map { _ =>
          // If successful, we simply redirect to the index page.
          Redirect(routes.OrderDetailController.index).flashing("success" -> "order detail created")
        }
      }
    )
  }


  def getOrderDetailById(id : String) = Action {
    Ok("return product, id:" + id)
  }

  def deleteOrderDetailById(id : String) = Action {
    Ok("delete product, id:" + id)
  }

  def updateOrderDetailById(id: String) = Action{
    Ok("update product, product id" + id)
  }
}

case class CreateOrderDetailForm(orderID: Int, productID:Int, quantity: Int)
