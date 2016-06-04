package com.talkie.client.common.events

trait EventListener[E <: Event] {

  def handleEvent(event: E): Boolean // event consumed?
}
