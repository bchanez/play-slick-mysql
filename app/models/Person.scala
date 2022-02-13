package models

import play.api.data.Form
import play.api.data.Forms.mapping
import play.api.data.Forms._

case class Person(id: Long, firstName: String, lastName: String);

case class PersonFormData(firstName: String, lastName: String)

object PersonForm {
  val form = Form {
    mapping(
      "firstName" -> nonEmptyText,
      "lastName" -> nonEmptyText
    )(PersonFormData.apply)(PersonFormData.unapply)
  }
}
