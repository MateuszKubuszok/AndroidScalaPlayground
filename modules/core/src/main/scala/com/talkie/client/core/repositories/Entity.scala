package com.talkie.client.core.repositories

import org.joda.time.DateTime

case class Id[E <: Entity[E]](value: Long)

case class SysInfo(
  version:   Long,
  createdOn: DateTime,
  updatedOn: DateTime
)

trait Entity[E <: Entity[E]] {

  type Data

  val id: Id[E]

  val data: Data

  val sys: SysInfo
}
