package com.talkie.client.core.context

import java.util.concurrent.{ Executors, ScheduledFuture, ScheduledExecutorService }

import com.talkie.client.core.events.{ EventListener, Event }
import com.talkie.client.core.logging.LoggerImpl
import com.talkie.client.core.scheduler.Job

import scala.collection.mutable
import scala.concurrent.{ Promise, ExecutionContext }
import scala.reflect.ClassTag

trait CoreContext { self: Context =>

  def eventListeners: mutable.Map[ClassTag[_ <: Event], mutable.Set[EventListener[_]]]

  def permissionsExecutionContext: ExecutionContext
  def permissionsRequests: mutable.Map[Int, Promise[Unit]]

  def schedulerExecutor: ScheduledExecutorService
  def schedulerJobs: mutable.Map[Job, ScheduledFuture[_]]
}

object CoreContextImpl {

  private val logger = new LoggerImpl(this.getClass.getSimpleName)

  val eventListeners = mutable.Map[ClassTag[_ <: Event], mutable.Set[EventListener[_]]]()

  val permissionsExecutionContext = ExecutionContext.fromExecutor(
    Executors.newFixedThreadPool(2),
    (e: Throwable) => logger error ("Permission Future exception", e)
  )
  val permissionsRequests = mutable.Map[Int, Promise[Unit]]()

  val schedulerExecutor = Executors.newScheduledThreadPool(2)
  val schedulerJobs = mutable.Map[Job, ScheduledFuture[_]]()
}

trait CoreContextImpl extends CoreContext { self: Context =>

  override val eventListeners = CoreContextImpl.eventListeners

  override val permissionsExecutionContext = CoreContextImpl.permissionsExecutionContext
  override val permissionsRequests = CoreContextImpl.permissionsRequests

  override val schedulerExecutor = CoreContextImpl.schedulerExecutor
  override val schedulerJobs = CoreContextImpl.schedulerJobs
}
