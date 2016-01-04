package com.talkie.client.domain.repositories.profiles

import com.talkie.client.core.repositories.{ DatabaseClientComponent, Id, SysInfo }
import com.talkie.client.domain.repositories.{ TypeMappers, DomainTableComponent }
import org.joda.time.DateTime
import scala.slick.driver.SQLiteDriver.simple._

trait ProfileTableComponent extends DomainTableComponent[Profile] {
  self: DatabaseClientComponent =>

  import TypeMappers._

  class ProfileTable(tag: Tag) extends DomainEntityTable(tag, "Profile") {

    def facebookId = column[FacebookId]("FacebookId")(facebookIdTypeMapper)

    type ShapeType = (Id[Profile], SysInfo, ProfileData)
    type TupleType = (Id[Profile], Long, DateTime, DateTime, FacebookId)

    def fullProjection = (id, version, createdAt, updatedAt, facebookId)

    override def * = {

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

  object ProfileQuery extends EntityTableQuery(new ProfileTable(_)) {

  }
}
