package models

import play.api.data.Form
import play.api.data.Forms.mapping
import play.api.data.Forms._
import play.api.libs.json.{OFormat, Json}

case class Person(id: Option[Long], firstName: String, lastName: String);
object PersonForm {
  val form = Form {
    mapping(
      "id" -> optional(longNumber),
      "firstName" -> nonEmptyText,
      "lastName" -> nonEmptyText
    )(Person.apply)(Person.unapply)
  }
}

object Person {
  implicit val personFormat: OFormat[Person] = Json.format[Person]
}
