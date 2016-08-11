package com.talkie.client.common.context

import com.talkie.client.common.logging.{ Logger, LoggerImpl }

import scalaz.State._
import scalaz._

trait Context {

  def owner: android.content.ContextWrapper

  def androidContext: android.content.Context

  def loggerFor(self: Any): Logger

  def withLocalState[T](block: LocalState => T): T
  def withLocalStateUpdated[T](state: State[LocalState, T]): T
  def withLocalStateUpdated[T](block: LocalState => (LocalState, T)): T
  def withLocalStateUpdated2[T](block: LocalState => State[LocalState, T]): T

  def withSharedState[T](block: SharedState => T): T
  def withSharedStateUpdated[T](state: State[SharedState, T]): T
  def withSharedStateUpdated[T](block: SharedState => (SharedState, T)): T
  def withSharedStateUpdated2[T](block: SharedState => State[SharedState, T]): T
}

object ContextImpl {

  private val logger = new LoggerImpl(this.getClass.getSimpleName)

  private var localState = LocalState()

  private var sharedState = SharedState()

  logger trace "Global Context created"

  def apply(owner: android.content.ContextWrapper): Context = new ContextImpl(
    owner,
    (self: Any) => new LoggerImpl(s"${owner.getClass.getSimpleName}:${self.getClass.getSimpleName}"),
    localState,
    (newLocalState: LocalState) => localState = newLocalState,
    sharedState,
    (newSharedState: SharedState) => sharedState = newSharedState
  )
}

class ContextImpl(
    override val owner: android.content.ContextWrapper,
    loggerFactory:      Any => Logger,
    getLocal:           => LocalState,
    putLocal:           LocalState => Unit,
    getShared:          => SharedState,
    putShared:          SharedState => Unit
) extends Context {

  require(owner != null, "Context cannot be initialized with an empty ContextWrapper")

  override lazy val androidContext = {
    require(owner.getBaseContext != null, "androidContext cannot be used before initialization is finished")
    owner.getBaseContext
  }

  override def loggerFor(self: Any) = loggerFactory(self)

  override def withLocalState[T](block: LocalState => T): T = block(getLocal)

  override def withLocalStateUpdated[T](state: State[LocalState, T]): T = {
    val (newState, result) = state(getLocal)
    putLocal(newState)
    result
  }

  override def withLocalStateUpdated[T](block: LocalState => (LocalState, T)): T =
    withLocalStateUpdated(State(block))

  override def withLocalStateUpdated2[T](block: LocalState => State[LocalState, T]): T =
    withLocalStateUpdated(init[LocalState] flatMap block)

  override def withSharedState[T](block: SharedState => T): T = block(getShared)

  override def withSharedStateUpdated[T](state: State[SharedState, T]): T = {
    val (newState, result) = state(getShared)
    putShared(newState)
    result
  }

  override def withSharedStateUpdated[T](block: SharedState => (SharedState, T)): T =
    withSharedStateUpdated(State(block))

  override def withSharedStateUpdated2[T](block: SharedState => State[SharedState, T]): T =
    withSharedStateUpdated(init[SharedState] flatMap block)
}
