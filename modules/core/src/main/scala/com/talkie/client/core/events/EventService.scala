package com.talkie.client.core.events

import com.talkie.client.common.events.{ EventListener, Event }
import com.talkie.client.common.services.{ Service => GenericService }

import scala.reflect.ClassTag
import scalaz.Free

sealed trait EventService[R] extends GenericService[R]
final case class GetEventListeners[E <: Event]()(implicit val eventType: ClassTag[E])
  extends EventService[Set[EventListener[E]]]
final case class NotifyEventListeners[E <: Event](event: E)(implicit val eventType: ClassTag[E])
  extends EventService[Boolean]
final case class RegisterEventListener[E <: Event](listener: EventListener[E])(implicit val eventType: ClassTag[E])
  extends EventService[Boolean]
final case class RemoveEventListener[E <: Event](listener: EventListener[E])(implicit val eventType: ClassTag[E])
  extends EventService[Boolean]

trait EventServiceFrees[S[R] >: EventService[R]] {

  def getListeners[E <: Event](implicit eventType: ClassTag[E]): Free[S, Set[EventListener[E]]] =
    Free.liftF(GetEventListeners()(eventType): S[Set[EventListener[E]]])

  def notifyEventListeners[E <: Event](event: E)(implicit eventType: ClassTag[E]): Free[S, Boolean] =
    Free.liftF(NotifyEventListeners(event): S[Boolean])

  def registerEventListener[E <: Event](listener: EventListener[E])(implicit eventType: ClassTag[E]): Free[S, Boolean] =
    Free.liftF(RegisterEventListener(listener): S[Boolean])

  def removeEventListener[E <: Event](listener: EventListener[E])(implicit eventType: ClassTag[E]): Free[S, Boolean] =
    Free.liftF(RemoveEventListener(listener): S[Boolean])
}

object EventService extends EventServiceFrees[EventService]
object Service extends EventServiceFrees[GenericService]
