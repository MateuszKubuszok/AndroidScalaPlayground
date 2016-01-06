package com.talkie.client.app.activities.common

import com.talkie.client.TypedFindView
import com.talkie.client.core.logging.LoggerComponent
import com.talkie.client.app.navigation.ManualNavigation
import com.talkie.client.core.services.ContextComponent

import scala.concurrent.Future

trait Controller
    extends TypedFindView
    with ContextComponent
    with LoggerComponent
    with ManualNavigation {
  self: RichActivity =>

  private implicit val ec = context.executionContext

  def asyncAction[T](action: => T): Unit = Future(action)

  def asyncAction[T](action: Future[T]): Unit = {}
}
