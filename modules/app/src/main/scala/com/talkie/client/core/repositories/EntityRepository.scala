package com.talkie.client.core.repositories

trait EntityRepository[E <: Entity[E]] {

  def fetch(ids: Iterable[Id[E]]): Iterable[E]
  def find(ids: Iterable[Id[E]]): Iterable[Option[E]]

  def create(datas: Iterable[E#Data]): Iterable[Option[E]]
  def update(entities: Iterable[E]): Iterable[E]
  def remove(ids: Iterable[Id[E]]): Iterable[Boolean]
}
