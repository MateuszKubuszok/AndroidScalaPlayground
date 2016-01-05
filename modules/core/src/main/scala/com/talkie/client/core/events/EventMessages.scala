package com.talkie.client.core.events

import scala.reflect.ClassTag

object EventMessages {

  case class GetListenersRequest[E <: Event]()(implicit val eventType: ClassTag[E])
  case class GetListenersResponse(listeners: Set[EventListener[_ <: Event]])

  case class NotifyEventListenersRequest[E <: Event](event: E)(implicit val eventType: ClassTag[E])
  case class NotifyEventListenersResponse(success: Boolean)

  case class RegisterEventListenerRequest[E <: Event](listener: EventListener[E])(implicit val eventType: ClassTag[E])
  case class RegisterEventListenerResponse(success: Boolean)

  case class RemoveEventListenerRequest[E <: Event](listener: EventListener[E])(implicit val eventType: ClassTag[E])
  case class RemoveEventListenerResponse(success: Boolean)
}
