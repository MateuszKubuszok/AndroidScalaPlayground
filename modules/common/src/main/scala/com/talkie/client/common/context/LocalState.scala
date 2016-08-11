package com.talkie.client.common.context

import com.talkie.client.common.events.{ EventListener, Event }

import scala.concurrent.Promise
import scala.reflect.ClassTag

case class LocalState(
  eventListeners:     Map[ClassTag[_ <: Event], Set[EventListener[_]]] = Map(),
  permissionRequests: Map[Int, Promise[Unit]]                          = Map()
)
