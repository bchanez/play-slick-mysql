package repositories

import scala.concurrent.{ExecutionContext, Future}
import javax.inject.Inject
import models.Person
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfigProvider
import play.db.NamedDatabase
import slick.jdbc.JdbcProfile

@Singleton
class PersonDAO @Inject() (dbConfigProvider: DatabaseConfigProvider)(implicit
    ec: ExecutionContext
) extends HasDatabaseConfigProvider[JdbcProfile] {
  import profile.api._

  private val personTable = TableQuery[PersonTable]

  private class PersonTable(tag: Tag) extends Table[Person](tag, "PERSON") {
    def id = column[Long]("ID", O.PrimaryKey, O.AutoInc)
    def firstName = column[String]("FIRST_NAME")
    def lastName = column[String]("LAST_NAME")

    def * =
      (id, firstName, lastName) <> ((Person.apply _).tupled, Person.unapply)
  }

  def insert(person: Person): Future[Person] = {
    db.run(personTable += person)
      .map(res => "Person successfully added")
      .recover { case ex: Exception =>
        ex.getCause.getMessage
      }
  }

  def update(person: Person) : Future[Person] = {
    // todo
  }

  def delete(id: Long): Future[Int] = {
    db.run(personTable.filter(_.id === id).delete)
  }

  def get(id: Long): Future[Option[User]] = {
    db.run(personTable.filter(_.id === id).result.headOption)
  }

  def list(): Future[Seq[Person]] = { db.run(personTable.result) }
}
