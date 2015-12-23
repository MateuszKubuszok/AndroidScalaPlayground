package com.talkie.client.services

import scala.concurrent.ExecutionContext

trait Context {

  val executionContext: ExecutionContext
}

trait ContextComponent {

  val context: Context
}

trait ContextComponentImpl extends ContextComponent {

  object context extends Context {

    val executionContext = ExecutionContext.Implicits.global
  }
}
