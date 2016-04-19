package com.talkie.client.core.repositories

import org.joda.time.DateTime
import scala.slick.driver.SQLiteDriver.simple._
import scala.slick.lifted.Tag

class EntityRepository[E <: Entity[E]] {
  import EntityMappers._

  abstract class EntityTable(tag: Tag, tableName: String)
      extends Table[(Id[E], SysInfo, E#Data)](tag, tableName) {

    val idName = tableName + "Id"

    def id = column[Id[E]](idName, O.PrimaryKey, O.AutoInc)(entityIdTypeMapper[E])

    def version = column[Long]("Version", O.DBType("integer"))
    def createdAt = column[DateTime]("CreateDate", O.DBType("text"))(dateTimeTypeMapper)
    def updatedAt = column[DateTime]("UpdateDate", O.DBType("text"))(dateTimeTypeMapper)

    def idProjection = Tuple1(id)
    def sysProjection = (version, createdAt, updatedAt)

  }

  abstract class EntityTableQuery(databaseClient: DatabaseClient, cons: Tag => EntityTable)
      extends TableQuery(cons) {

    val db = databaseClient.db

    def find(ids: Traversable[Id[E]]): Seq[(Id[E], SysInfo, E#Data)] = db withSession { implicit session =>
      this.filter(_.id inSet ids).list
    }
  }
}
