package controllers

import javax.inject._
import models.Person
import repositories.PersonRepository
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.validation.Constraints._
import play.api.i18n._
import play.api.libs.json.Json
import play.api.mvc._
import scala.concurrent.{Future, ExecutionContext}

@Singleton
class PersonController @Inject() (
    personRepo: PersonRepository,
    cc: MessagesControllerComponents
)(implicit ec: ExecutionContext)
    extends MessagesAbstractController(cc) {

  val personForm: Form[CreatePersonForm] = Form {
    mapping(
      "firstName" -> nonEmptyText,
      "lastName" -> nonEmptyText
    )(CreatePersonForm.apply)(CreatePersonForm.unapply)
  }

  def insertPerson = Action.async { implicit request =>
    personForm
      .bindFromRequest()
      .fold(
        errorForm => {
          Future.successful(Ok(views.html.index(errorForm)))
        },
        person => {
          personRepo.insert(person.firstName, person.lastName).map { person =>
            Ok(person.firstName + " " + person.lastName + " added")
          }
        }
      )
  }

  // def updatePerson = Action.async { implicit request =>
  //   personForm
  //     .bindFromRequest()
  //     .fold(
  //       errorForm => {
  //         Future.successful(Ok(views.html.index(errorForm)))
  //       },
  //       person => {
  //         personRepo.update(person.id, person.firstName, person.lastName).map {
  //           _ =>
  //             Redirect(routes.PersonController.index)
  //               .flashing("success" -> "a person has been updated")
  //         }
  //       }
  //     )
  // }

  // // todo
  // def deletePerson = Action { implicit request: Request[AnyContent] =>
  //   Ok(views.html.index())
  // }

  // // todo
  // def getPerson = Action { implicit request: Request[AnyContent] =>
  //   Ok(views.html.index())
  // }

  def getAllPerson = Action.async { implicit request =>
    personRepo.list().map { person =>
      Ok(Json.toJson(person))
    }
  }
}

case class CreatePersonForm(firstName: String, lastName: String)
