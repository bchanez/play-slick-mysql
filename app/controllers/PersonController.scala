package controllers

import dao.PersonDAO
import javax.inject.Inject
import models.{Person, PersonForm}
import play.api.mvc._
import scala.concurrent.ExecutionContext
import play.api.Logging

@Singleton
class PersonController @Inject() (
    personDAO: PersonDAO,
    mcc: MessagesControllerComponents
)(implicit executionContext: ExecutionContext)
    extends MessagesAbstractController(mcc) with Logging  {

  def insertPerson = Action.async { implicit request =>
    PersonForm.form
      .bindFromRequest()
      .fold(
        errorForm => {
          logger.warn(s"Form submission with error: ${errorForm.errors}")
          Future.successful(Ok(views.html.index(errorForm)))
        },
        data => {
          val newPerson = Person(0, data.firstName, data.lastName)
          personDAO.insert(newPerson).map { person =>
            Ok(Json.toJson(person))
          }
        }
      )
  }

  def updatePerson = Action.async { implicit request =>
    PersonForm.form
      .bindFromRequest()
      .fold(
        errorForm => {
          logger.warn(s"Form submission with error: ${errorForm.errors}")
          Future.successful(Ok(views.html.index(errorForm)))
        },
        data => {
          val newPerson = Person(0, data.firstName, data.lastName)
          personDAO.update(newPerson).map { person =>
            Ok(Json.toJson(person))
          }
        }
      )
  }

  def deletePerson(id: Long) = Action { implicit request: Request[AnyContent] =>
    personDAO.delete(id) map { res =>
      Ok("person with ID " + id + " deleted")
    }
  }

  def getPerson(id: Long) = Action { implicit request: Request[AnyContent] =>
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
