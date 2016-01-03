package com.talkie.client.core

import scala.language.implicitConversions

package object repositories {

  implicit def idToLong[E <: Entity[E]](id: Id[E]): Long = id.value

  implicit def longsToIds[E <: Entity[E]](ids: Iterable[Long]): Seq[Id[E]] = ids.map(Id[E](_)).toSeq

  implicit def idsToLongs[E <: Entity[E]](ids: Iterable[Id[E]]): Seq[Long] = ids.map(_.value).toSeq

  implicit def longToId[E <: Entity[E]](id: Long): Id[E] = Id[E](id)
}
