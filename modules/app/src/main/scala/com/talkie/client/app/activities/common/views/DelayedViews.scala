package com.talkie.client.app.activities.common.views

import android.view.View
import com.talkie.client.app.activities.common.RichActivity

import scala.concurrent.duration.Duration.Zero
import scala.concurrent.{ Await, Promise }

trait DelayedViews {
  self: RichActivity =>

  def delay[T]() = Promise[T]()
  def getDelayed[T](delayed: Promise[T]) = Await.result(delayed.future, Zero)

  def initiateWith[T <: View](delayed: Promise[T])(block: => T) {
    onCreate {
      val layout = block
      delayed success layout
      setContentView(layout)
    }
  }

  implicit class DelayWrapper[T <: View](delayed: Promise[T]) {

    def resolve(block: => T) = {
      val value = block
      delayed success value
      value
    }
  }
}
