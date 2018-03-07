package {{ package.name }}.controller

import javax.inject.{Inject, Singleton}

import {{ package.name }}.dto.PostOrderItem
import play.api.libs.json._
import play.api.mvc.{AbstractController, Action, ControllerComponents}
import {{ package.name }}.service.OrderItemService

import scala.concurrent.{ExecutionContext, Future}

@Singleton()
class OrderItemController @Inject()(cc: ControllerComponents, orderItemService: OrderItemService)(
  implicit executionContext: ExecutionContext)
  extends AbstractController(cc) {

  def getAll(page: Int, size: Int) = Action.async {
    orderItemService.getAll(page, size) map (result => Ok(Json.toJson(result)))
  }

  def getShoppingCartItemsByUserId(userId: Long) = Action.async {
    orderItemService.getShoppingCartItemsByUserId(userId) map (result => Ok(Json.toJson(result)))
  }

   def getShoppingCartByUserId(userId: Long) = Action.async {
    orderItemService.getShoppingCartByUserId(userId) map (result => Ok(Json.toJson(result.id)))
  }

  def getByUserId(id: Long, page: Int, size: Int) = Action.async {
    orderItemService.getByUserId(id, page, size) map (result => Ok(Json.toJson(result)))
  }

  def add: Action[JsValue] = Action.async(parse.json) { request =>
    val optionalOrderItem = request.body.validate[PostOrderItem]
    optionalOrderItem match {
      case JsSuccess(postOrderItem: PostOrderItem, _) =>
        orderItemService.insert(postOrderItem) map { result =>
          Created(Json.toJson(result))
        }
      case _: JsError => Future.successful(BadRequest)
    }
  }

  def update(id: Long): Action[JsValue] = Action.async(parse.json) { request =>
    val optionalOrderItem = request.body.validate[PostOrderItem]
    optionalOrderItem match {
      case JsSuccess(postOrderItem: PostOrderItem, _) =>
        orderItemService.update(id, postOrderItem) map { result =>
          Ok(Json.toJson(result))
        }
      case _: JsError => Future.successful(BadRequest)
    }
  }

  def delete(id: Long) = Action.async {
    orderItemService.delete(id) map {
      case x if x < 1 => NotFound
      case _          => Ok
    }
  }
}
