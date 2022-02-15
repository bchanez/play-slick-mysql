package controllers

import dao.PersonDAO
import javax.inject.Inject
import models.{Person, PersonForm}
import play.api.mvc._
import scala.concurrent.{ExecutionContext, Future}
import play.api.Logging
import play.api.libs.json.Json

class PersonController @Inject() (
    personDAO: PersonDAO,
    controllerComponents: ControllerComponents
)(implicit executionContext: ExecutionContext)
    extends AbstractController(controllerComponents)
    with Logging {

  def insertPerson = Action.async { implicit request =>
    PersonForm.form
      .bindFromRequest()
      .fold(
        errorForm => {
          logger.warn(s"Form submission with error: ${errorForm.errors}")
          Future.successful(Ok("bad request"))
        },
        person => {
          for {
            _ <- personDAO.insert(person)
          } yield Ok(s"person '${person.firstName} ${person.lastName}' has been added")
        }
      )
  }

  def updatePerson(id: Long) = Action.async { implicit request =>
    PersonForm.form
      .bindFromRequest()
      .fold(
        errorForm => {
          logger.warn(s"Form submission with error: ${errorForm.errors}")
          Future.successful(Ok("bad request"))
        },
        person => {
          for {
            _ <- personDAO.update(id, person)
          } yield Ok(s"person '${person.firstName} ${person.lastName}' has been updated"
          )
        }
      )
  }

  def deletePerson(id: Long) = Action.async { implicit request: Request[AnyContent] =>
    for {
      _ <- personDAO.delete(id)
    } yield Ok(s"person with ID '${id}' has been deleted")
  }

  def getPerson(id: Long) = Action.async { implicit request: Request[AnyContent] =>
    personDAO.get(id).map { person =>
      Ok(Json.toJson(person))
    }
  }

  def getAllPerson = Action.async { implicit request =>
    personDAO.list().map { person =>
      Ok(Json.toJson(person))
    }
  }
}
