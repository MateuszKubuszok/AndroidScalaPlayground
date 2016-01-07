package com.talkie.client.app.activities.common.utils

import android.view.View

import scala.concurrent.duration.Duration.Zero
import scala.concurrent.{ Await, Promise }

trait DelayedViews {

  def delay[T <: View]() = Promise[T]()
  def getDelayed[T <: View](delayed: Promise[T]) = Await.result(delayed.future, Zero)
}
