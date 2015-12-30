package com.talkie.client.core.services

import com.talkie.client.core.logging.LoggerComponent

import scala.concurrent.ExecutionContext

trait Context {

  val executionContext: ExecutionContext
}

trait ContextComponent {

  val context: Context
}

trait ContextComponentImpl extends ContextComponent {
  self: LoggerComponent =>

  object context extends Context {

    val executionContext = ExecutionContext.fromExecutor(
      ContextExecutor,
      (e: Throwable) => logger error ("Unhandled Future exception", e)
    )
  }
}
