package models

case class Person(id: Long, firstName:String, lastName:String, age:Integer);

object Person {
  implicit val personFormat: OFormat[Person] = Json.format[Person]
}
