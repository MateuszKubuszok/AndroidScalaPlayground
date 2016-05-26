package com.talkie.client.core.context

import java.util.concurrent.{ Executors, ScheduledExecutorService }

import com.talkie.client.core.logging.{ Logger, LoggerImpl }
import com.talkie.client.core.services.ServiceExecutor

import scala.concurrent.ExecutionContext
import scalaz.State._
import scalaz._

trait Context {

  def owner: android.content.ContextWrapper

  def androidContext: android.content.Context

  def serviceExecutionContext: ExecutionContext

  def permissionsExecutionContext: ExecutionContext

  def schedulerExecutor: ScheduledExecutorService

  def loggerFor(self: Any): Logger

  def withSharedState[T](block: SharedState => T): T
  def withSharedStateUpdated[T](state: State[SharedState, T]): T
  def withSharedStateUpdated[T](block: SharedState => (SharedState, T)): T
  def withSharedStateUpdated2[T](block: SharedState => State[SharedState, T]): T
}

object ContextImpl {

  private val logger = new LoggerImpl(this.getClass.getSimpleName)

  private val serviceExecutionContext = ExecutionContext.fromExecutor(
    ServiceExecutor,
    (e: Throwable) => logger error ("Unhandled Future exception", e)
  )

  private val permissionsExecutionContext = ExecutionContext.fromExecutor(
    Executors.newFixedThreadPool(2),
    (e: Throwable) => logger error ("Permission Future exception", e)
  )

  private val schedulerExecutor = Executors.newScheduledThreadPool(2)

  private var sharedState = SharedState()

  logger trace "Global Context created"

  def apply(owner: android.content.ContextWrapper): Context = new ContextImpl(
    owner,
    serviceExecutionContext,
    permissionsExecutionContext,
    schedulerExecutor,
    (self: Any) => new LoggerImpl(s"${owner.getClass.getSimpleName}:${self.getClass.getSimpleName}"),
    sharedState,
    (newSharedState: SharedState) => sharedState = newSharedState
  )
}

class ContextImpl(
    override val owner:                       android.content.ContextWrapper,
    override val serviceExecutionContext:     ExecutionContext,
    override val permissionsExecutionContext: ExecutionContext,
    override val schedulerExecutor:           ScheduledExecutorService,
    loggerFactory:                            Any => Logger,
    get:                                      => SharedState,
    put:                                      SharedState => Unit
) extends Context {

  override val androidContext = owner.getBaseContext

  override def loggerFor(self: Any) = loggerFactory(self)

  override def withSharedState[T](block: SharedState => T): T = block(get)

  override def withSharedStateUpdated[T](state: State[SharedState, T]): T = {
    val (newState, result) = state(get)
    put(newState)
    result
  }

  override def withSharedStateUpdated[T](block: SharedState => (SharedState, T)): T =
    withSharedStateUpdated(State(block))

  override def withSharedStateUpdated2[T](block: SharedState => State[SharedState, T]): T =
    withSharedStateUpdated(init[SharedState] flatMap block)
}
