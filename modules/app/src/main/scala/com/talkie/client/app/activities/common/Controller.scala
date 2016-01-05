package com.talkie.client.app.activities.common

import android.app.Activity
import com.talkie.client.core.logging.LoggerComponent
import com.talkie.client.app.navigation.ManualNavigation
import com.talkie.client.core.services.ContextComponent
import com.talkie.client.app.views.ActivityViews

import scala.concurrent.Future

trait Controller
    extends ActivityViews
    with ContextComponent
    with LoggerComponent
    with ManualNavigation {
  self: Activity =>

  private implicit val ec = context.executionContext

  def asyncAction[T](action: => T): Unit = Future(action)

  def asyncAction[T](action: Future[T]): Unit = {}
}