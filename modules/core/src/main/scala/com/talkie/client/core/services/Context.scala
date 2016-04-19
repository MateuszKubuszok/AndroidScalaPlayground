package com.talkie.client.core.services

import java.util.concurrent.{ Executors, ScheduledFuture, ScheduledExecutorService }

import android.app.Activity
import com.talkie.client.core.events.{ EventListener, Event }
import com.talkie.client.core.logging.{ Logger, LoggerImpl }
import com.talkie.client.core.scheduler.Job

import scala.collection.mutable
import scala.concurrent.{ Promise, ExecutionContext }
import scala.reflect.ClassTag

trait Context {

  def activity: Activity

  def loggerFor(self: Any): Logger

  def eventListeners: mutable.Map[ClassTag[_ <: Event], mutable.Set[EventListener[_]]]

  def serviceExecutionContext: ExecutionContext

  def permissionsExecutionContext: ExecutionContext
  def permissionsRequests: mutable.Map[Int, Promise[Unit]]

  def schedulerExecutor: ScheduledExecutorService
  def schedulerJobs: mutable.Map[Job, ScheduledFuture[_]]
}

object Context extends Context {

  def from(activity: Activity): Context = new ContextImpl(activity)

  private val logger = loggerFor(this)

  def activity = ???

  override def loggerFor(self: Any) = new LoggerImpl(self.getClass.getSimpleName)

  val eventListeners = mutable.Map[ClassTag[_ <: Event], mutable.Set[EventListener[_]]]()

  val serviceExecutionContext = ExecutionContext.fromExecutor(
    ContextExecutor(this),
    (e: Throwable) => logger error ("Unhandled Future exception", e)
  )

  val permissionsExecutionContext = ExecutionContext.fromExecutor(
    Executors.newFixedThreadPool(2),
    (e: Throwable) => logger error ("Permission Future exception", e)
  )
  val permissionsRequests = mutable.Map[Int, Promise[Unit]]()

  val schedulerExecutor = Executors.newScheduledThreadPool(2)
  val schedulerJobs = mutable.Map[Job, ScheduledFuture[_]]()
}

class ContextImpl(override val activity: Activity) extends Context {

  override def loggerFor(self: Any) = new LoggerImpl(s"${activity.getClass.getSimpleName}:${self.getClass.getSimpleName}")

  override val eventListeners = Context.eventListeners

  override val serviceExecutionContext = Context.serviceExecutionContext

  override val permissionsExecutionContext = Context.permissionsExecutionContext
  override val permissionsRequests = Context.permissionsRequests

  override val schedulerExecutor = Context.schedulerExecutor
  override val schedulerJobs = Context.schedulerJobs
}
