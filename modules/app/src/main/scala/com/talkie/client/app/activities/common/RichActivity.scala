package com.talkie.client.app.activities.common

import android.content.Intent
import android.os.Bundle
import org.scaloid.common.SActivity

import scala.collection.mutable.ArrayBuffer

trait RichActivity extends SActivity {

  override def onPostCreate(savedInstanceState: Bundle) {
    super.onPostCreate(savedInstanceState)
    onPostCreateBodies.foreach(_())
  }

  protected val onPostCreateBodies = new ArrayBuffer[() => Any]

  def onPostCreate(body: => Any) = {
    val el = body _
    onPostCreateBodies += el
    el
  }

  override def onRestart {
    super.onRestart()
    onRestartBodies.foreach(_())
  }

  protected val onRestartBodies = new ArrayBuffer[() => Any]

  def onRestart(body: => Any) = {
    val el = body _
    onRestartBodies += el
    el
  }

  override def onActivityResult(requestCode: Int, resultCode: Int, data: Intent) = {
    super.onActivityResult(requestCode, resultCode, data)
    onActivityResultBodies.foreach(_(requestCode, resultCode, data))
  }

  protected val onActivityResultBodies = new ArrayBuffer[(Int, Int, Intent) => Any]

  def onActivityResult(body: (Int, Int, Intent) => Any) = {
    onActivityResultBodies += body
    body
  }
}
