package com.talkie.client.activities.common

import android.app.Activity
import com.talkie.client.navigation.ManualNavigation
import com.talkie.client.services.{ContextComponent, LoggerComponent}
import com.talkie.client.views.ActivityViews

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
