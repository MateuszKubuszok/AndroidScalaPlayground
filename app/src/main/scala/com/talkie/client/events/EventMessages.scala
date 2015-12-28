package com.talkie.client.events

object EventMessages {

  case class NotifyEventListenersRequest(event: Event)
  case class NotifyEventListenersResponse(success: Boolean)

  case class RegisterEventListenerRequest[E <: Event](listener: EventListener[E], event: E)
  case class RegisterEventListenerResponse(success: Boolean)

  case class RemoveEventListenerRequest[E <: Event](listener: EventListener[E], event: E)
  case class RemoveEventListenerResponse(success: Boolean)
}
