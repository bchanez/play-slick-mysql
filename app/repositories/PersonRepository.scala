package repositories

import models.Person
import javax.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile
import scala.concurrent.{ Future, ExecutionContext }

@Singleton
class PersonRepository @Inject() (dbConfigProvider: DatabaseConfigProvider)(
    implicit ec: ExecutionContext
) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  private class PeopleTable(tag: Tag) extends Table[Person](tag, "people") {
    def id = column[Long]("ID", O.PrimaryKey, O.AutoInc)
    def firstName = column[String]("FIRST_NAME")
    def lastName = column[String]("LAST_NAME")

    def * =
      (id, firstName, lastName) <> ((Person.apply _).tupled, Person.unapply)
  }

  // TODO why people ?
  private val people = TableQuery[PeopleTable]

  def insert(firstName: String, lastName: String): Future[Person] = db.run {
    (people.map(p => (p.firstName, p.lastName))
      returning people.map(_.id)
      into ((firstName_lastName, id) =>
        Person(id, firstName_lastName._1, firstName_lastName._2)
      )) += (firstName, lastName)
  }

  def update() {}

  def delete() {}

  def get() {}

  def list(): Future[Seq[Person]] = db.run {
    people.result
  }
}
