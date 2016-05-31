package com.talkie.client.core.events

import com.talkie.client.core.services.Service

import scala.reflect.ClassTag
import scalaz.Free

sealed trait EventService[R] extends Service[R]
final case class GetEventListeners[E <: Event]()(implicit val eventType: ClassTag[E])
  extends EventService[Set[EventListener[E]]]
final case class NotifyEventListeners[E <: Event](event: E)(implicit val eventType: ClassTag[E])
  extends EventService[Boolean]
final case class RegisterEventListener[E <: Event](listener: EventListener[E])(implicit val eventType: ClassTag[E])
  extends EventService[Boolean]
final case class RemoveEventListener[E <: Event](listener: EventListener[E])(implicit val eventType: ClassTag[E])
  extends EventService[Boolean]

object EventService {

  def getListeners[E <: Event](implicit eventType: ClassTag[E]): Free[EventService, Set[EventListener[E]]] =
    Free.liftF(GetEventListeners()(eventType): EventService[Set[EventListener[E]]])

  def notifyEventListeners[E <: Event](event: E)(implicit eventType: ClassTag[E]): Free[EventService, Boolean] =
    Free.liftF(NotifyEventListeners(event): EventService[Boolean])

  def registerEventListener[E <: Event](listener: EventListener[E])(implicit eventType: ClassTag[E]): Free[EventService, Boolean] =
    Free.liftF(RegisterEventListener(listener): EventService[Boolean])

  def removeEventListener[E <: Event](listener: EventListener[E])(implicit eventType: ClassTag[E]): Free[EventService, Boolean] =
    Free.liftF(RemoveEventListener(listener): EventService[Boolean])
}
