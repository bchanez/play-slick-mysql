package models

import play.api.libs.json.{OFormat, Json}

case class Person(id: Long, firstName: String, lastName: String);

object Person {
  implicit val personFormat: OFormat[Person] = Json.format[Person]
}
