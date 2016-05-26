package com.talkie.client.core.context

import java.util.concurrent.ScheduledFuture

import com.talkie.client.core.events.{ Event, EventListener }
import com.talkie.client.core.scheduler.Job

import scala.concurrent.Promise
import scala.reflect.ClassTag

case class SharedState(
  eventListeners:     Map[ClassTag[_ <: Event], Set[EventListener[_]]] = Map(),
  permissionRequests: Map[Int, Promise[Unit]]                          = Map(),
  scheduledJobs:      Map[Job, ScheduledFuture[_]]                     = Map()
)
