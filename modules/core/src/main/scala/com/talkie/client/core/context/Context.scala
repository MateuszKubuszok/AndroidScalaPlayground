package com.talkie.client.core.context

import com.talkie.client.core.logging.{ Logger, LoggerImpl }
import com.talkie.client.core.services.ServiceExecutor

import scala.concurrent.ExecutionContext

trait Context {

  def owner: android.content.ContextWrapper

  def androidContext: android.content.Context

  def loggerFor(self: Any): Logger

  def serviceExecutionContext: ExecutionContext
}

object ContextImpl {

  private val logger = new LoggerImpl(this.getClass.getSimpleName)

  val serviceExecutionContext = ExecutionContext.fromExecutor(
    ServiceExecutor,
    (e: Throwable) => logger error ("Unhandled Future exception", e)
  )
}

class ContextImpl(override val owner: android.content.ContextWrapper) extends Context {

  private val logger = loggerFor(this)

  override val androidContext = owner.getBaseContext

  override def loggerFor(self: Any) = new LoggerImpl(s"${owner.getClass.getSimpleName}:${self.getClass.getSimpleName}")

  override lazy val serviceExecutionContext = ContextImpl.serviceExecutionContext
}
