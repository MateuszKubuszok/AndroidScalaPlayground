package com.talkie.client.common.context

import java.util.concurrent.ScheduledFuture

import com.talkie.client.common.events.{ EventListener, Event }
import com.talkie.client.common.scheduler.Job

import scala.concurrent.Promise
import scala.reflect.ClassTag

case class SharedState(
  eventListeners:     Map[ClassTag[_ <: Event], Set[EventListener[_]]] = Map(),
  permissionRequests: Map[Int, Promise[Unit]]                          = Map(),
  scheduledJobs:      Map[Job, ScheduledFuture[_]]                     = Map()
)
