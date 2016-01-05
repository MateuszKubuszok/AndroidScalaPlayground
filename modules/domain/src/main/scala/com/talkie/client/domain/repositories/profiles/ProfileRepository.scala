package com.talkie.client.domain.repositories.profiles

import com.talkie.client.core.repositories.{ Id, EntityRepository }

trait ProfileRepository extends EntityRepository[Profile] {

  def fetch(ids: Iterable[Id[Profile]]): Iterable[Profile]
  def find(ids: Iterable[Id[Profile]]): Iterable[Option[Profile]]

  def create(datas: Iterable[ProfileData]): Iterable[Option[Profile]]
  def update(entities: Iterable[Profile]): Iterable[Profile]
  def remove(ids: Iterable[Id[Profile]]): Iterable[Boolean]
}

trait ProfileRepositoryComponent {

  def profileRepository: ProfileRepository
}

trait ProfileRepositoryComponentImpl extends ProfileRepositoryComponent {

  object profileRepository extends ProfileRepository {

    override def fetch(ids: Iterable[Id[Profile]]): Iterable[Profile] = ???

    override def update(entities: Iterable[Profile]): Iterable[Profile] = ???

    override def remove(ids: Iterable[Id[Profile]]): Iterable[Boolean] = ???

    override def find(ids: Iterable[Id[Profile]]): Iterable[Option[Profile]] = ???

    override def create(datas: Iterable[ProfileData]): Iterable[Option[Profile]] = ???
  }
}