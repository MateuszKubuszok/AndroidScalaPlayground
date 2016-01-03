package com.talkie.client.domain.repositories

import com.talkie.client.core.repositories.{ DatabaseClientComponent, Entity, EntityTableComponent }
import slick.lifted.Tag

trait DomainTableComponent[E <: Entity[E]] extends EntityTableComponent[E] {
  self: DatabaseClientComponent =>

  abstract class DomainEntityTable(tag: Tag, tableName: String)
    extends EntityTable(tag, tableName)
}
