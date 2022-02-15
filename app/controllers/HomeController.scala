package controllers

import javax.inject._
import play.api._
import play.api.mvc._

import scala.concurrent.ExecutionContext

@Singleton
class HomeController @Inject() (val controllerComponents: ControllerComponents)
    extends BaseController {

  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(
      "POST    /person\n"
        + "PUT     /person/:id\n"
        + "DELETE  /person/:id\n"
        + "GET     /person/:id\n"
        + "GET     /person/all\n"
    )
  }
}
