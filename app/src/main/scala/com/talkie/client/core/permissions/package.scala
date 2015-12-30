package com.talkie.client.core

import java.util.concurrent.Executors

import com.talkie.client.core.logging.LoggerComponentImpl

import scala.concurrent.ExecutionContext

package object permissions extends LoggerComponentImpl {

  val PermissionsExecutionContext = ExecutionContext.fromExecutor(
    Executors.newFixedThreadPool(2),
    (e: Throwable) => logger error ("Permission Future exception", e)
  )
}
