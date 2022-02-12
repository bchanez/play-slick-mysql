package controllers

import javax.inject._

import models._
import play.api.mvc._

import scala.concurrent.ExecutionContext

class PersonController @Inject()(repo: PersonRepository,
                                  cc: MessagesControllerComponents
                                )(implicit ec: ExecutionContext)
  extends MessagesAbstractController(cc) {

  val personForm: Form[CreatePersonForm] = Form {
    mapping(
      "firstName" -> nonEmptyText,
      "lastName" -> nonEmptyText,
      "age" -> number.verifying(min(1), max(150))
    )(CreatePersonForm.apply)(CreatePersonForm.unapply)
  }

  def index = Action { implicit request =>
    Ok(views.html.index(personForm))
  }

  def getPersonList = Action.async { implicit request =>
  }

  def getPerson = Action.async { implicit request =>

  }

  def addPerson = Action.async { implicit request =>

  }

  def updatePerson = Action.async { implicit request =>

  }

  def deletePerson = Action.async { implicit request =>

  }
}
