package com.talkie.client.domain.repositories

import com.talkie.client.core.repositories.EntityMappers
import com.talkie.client.domain.repositories.profiles.FacebookId

import scala.slick.driver.SQLiteDriver.simple._

trait TypeMappers extends EntityMappers {

  implicit def facebookIdTypeMapper = MappedColumnType.base[FacebookId, Long](_.value, FacebookId)
}

object TypeMappers extends TypeMappers
