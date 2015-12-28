package com.talkie.client.events

trait EventListener[E <: Event] {

  def handleEvent(event: E): Boolean // event consumed?
}
