package com.talkie.client.domain.profile

import com.talkie.client.core.repositories._
import com.talkie.client.domain.repositories.TypeMappers._
import org.joda.time.DateTime

import scala.slick.driver.SQLiteDriver.simple._

trait ProfileRepository extends CRUDEntityRepository[Profile] {

  def fetch(ids: Iterable[Id[Profile]]): Iterable[Profile]
  def find(ids: Iterable[Id[Profile]]): Iterable[Option[Profile]]

  def create(datas: Iterable[ProfileData]): Iterable[Option[Profile]]
  def update(entities: Iterable[Profile]): Iterable[Profile]
  def remove(ids: Iterable[Id[Profile]]): Iterable[Boolean]
}

class ProfileRepositoryImpl(databaseClient: DatabaseClient) extends EntityRepository[Profile] with ProfileRepository {

  private class ProfileTable(tag: Tag) extends EntityTable(tag, "Profile") {

    def facebookId = column[FacebookId]("FacebookId")(facebookIdTypeMapper)

    def fullProjection = (id, version, createdAt, updatedAt, facebookId)

    override def * = {

      type ShapeType = (Id[Profile], SysInfo, ProfileData)
      type TupleType = (Id[Profile], Long, DateTime, DateTime, FacebookId)

      def tuple2Shape(record: TupleType): ShapeType = {
        val id = record._1
        val sysInfo = SysInfo.tupled((record._2, record._3, record._4))
        val data = ProfileData.apply(record._5)
        (id, sysInfo, data)
      }

      def shape2Tuple(shape: ShapeType): Option[TupleType] = {
        val (id, sysInfo, data) = shape
        Option((id, sysInfo.version, sysInfo.createdOn, sysInfo.updatedOn, data.facebookId))
      }

      fullProjection <> (tuple2Shape, shape2Tuple)
    }
  }

  private class ProfileQuery extends EntityTableQuery(databaseClient, new ProfileTable(_))

  override def fetch(ids: Iterable[Id[Profile]]): Iterable[Profile] = ???

  override def update(entities: Iterable[Profile]): Iterable[Profile] = ???

  override def remove(ids: Iterable[Id[Profile]]): Iterable[Boolean] = ???

  override def find(ids: Iterable[Id[Profile]]): Iterable[Option[Profile]] = ???

  override def create(datas: Iterable[ProfileData]): Iterable[Option[Profile]] = ???
}
