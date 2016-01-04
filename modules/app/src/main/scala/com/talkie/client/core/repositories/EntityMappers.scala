package com.talkie.client.core.repositories

import org.joda.time.DateTime
import scala.slick.driver.SQLiteDriver.simple._

trait EntityMappers {

  implicit def entityIdTypeMapper[E <: Entity[E]] = MappedColumnType.base[Id[E], Long](_.value, Id[E])

  implicit def dateTimeTypeMapper = MappedColumnType.base[DateTime, String](_.toString, new DateTime(_))
}

object EntityMappers extends EntityMappers
