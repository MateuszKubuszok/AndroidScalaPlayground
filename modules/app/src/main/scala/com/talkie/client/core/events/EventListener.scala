package com.talkie.client.core.events

trait EventListener[E <: Event] {

  def handleEvent(event: E): Boolean // event consumed?
}
