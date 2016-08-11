package com.talkie.client.core.events

import com.talkie.client.common.events.{ Event, EventListener }

import scala.reflect.ClassTag
import scalaz.{ :<:, Free }

sealed trait EventService[R]

object EventService {

  final case class GetEventListeners[E <: Event]()(implicit val eventType: ClassTag[E])
    extends EventService[Set[EventListener[E]]]
  final case class NotifyEventListeners[E <: Event](event: E)(implicit val eventType: ClassTag[E])
    extends EventService[Boolean]
  final case class RegisterEventListener[E <: Event](listener: EventListener[E])(implicit val eventType: ClassTag[E])
    extends EventService[Boolean]
  final case class RemoveEventListener[E <: Event](listener: EventListener[E])(implicit val eventType: ClassTag[E])
    extends EventService[Boolean]

  class Ops[S[_]](implicit s0: EventService :<: S) {
    def getListeners[E <: Event](implicit eventType: ClassTag[E]): Free[S, Set[EventListener[E]]] =
      Free.liftF(s0.inj(GetEventListeners()))

    def notifyEventListeners[E <: Event](event: E)(implicit eventType: ClassTag[E]): Free[S, Boolean] =
      Free.liftF(s0.inj(NotifyEventListeners(event)))

    def registerEventListener[E <: Event](listener: EventListener[E])(implicit eventType: ClassTag[E]): Free[S, Boolean] =
      Free.liftF(s0.inj(RegisterEventListener(listener)))

    def removeEventListener[E <: Event](listener: EventListener[E])(implicit eventType: ClassTag[E]): Free[S, Boolean] =
      Free.liftF(s0.inj(RemoveEventListener(listener)))
  }
}
