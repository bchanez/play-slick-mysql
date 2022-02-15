package dao

import scala.concurrent.{ExecutionContext, Future}
import javax.inject.{ Inject, Singleton }
import models.Person
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfigProvider
import play.db.NamedDatabase
import slick.jdbc.JdbcProfile

@Singleton()
class PersonDAO @Inject() (protected val dbConfigProvider: DatabaseConfigProvider)(implicit
    executionContext: ExecutionContext
) extends HasDatabaseConfigProvider[JdbcProfile] {
  import profile.api._

  private val personTable = TableQuery[PersonTable]

  private class PersonTable(tag: Tag) extends Table[Person](tag, "PERSON") {
    def id = column[Long]("ID", O.PrimaryKey, O.AutoInc)
    def firstName = column[String]("FIRST_NAME")
    def lastName = column[String]("LAST_NAME")

    def * =
      (id.?, firstName, lastName) <> ((Person.apply _).tupled, Person.unapply)
  }

  def insert(person: Person): Future[Unit] = {
    db.run(personTable += person).map(_ => ())
  }

  def update(id: Long, person: Person): Future[Unit] = {
    val personToUpdate: Person = person.copy(Some(id))
    db.run(personTable.filter(_.id === id).update(personToUpdate)).map(_ => ())
  }

  def delete(id: Long): Future[Unit] = {
    db.run(personTable.filter(_.id === id).delete).map(_ => ())
  }

  def get(id: Long): Future[Option[Person]] = {
    db.run(personTable.filter(_.id === id).result.headOption)
  }

  def list(): Future[Seq[Person]] = { db.run(personTable.result) }
}
