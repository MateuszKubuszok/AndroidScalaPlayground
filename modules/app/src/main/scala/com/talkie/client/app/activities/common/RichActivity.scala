package com.talkie.client.app.activities.common

import android.content.Intent
import android.os.Bundle
import org.scaloid.common.SActivity

import scala.collection.mutable.ArrayBuffer

trait RichActivity extends SActivity {

  override protected def onPostCreate(savedInstanceState: Bundle) {
    super.onPostCreate(savedInstanceState)
    onPostCreateBodies.foreach(_())
  }

  protected val onPostCreateBodies = new ArrayBuffer[() => Any]

  protected def onPostCreate(body: => Any) = {
    val el = body _
    onPostCreateBodies += el
    el
  }

  override protected def onRestart() {
    super.onRestart()
    onRestartBodies.foreach(_())
  }

  protected val onRestartBodies = new ArrayBuffer[() => Any]

  protected def onRestart(body: => Any) = {
    val el = body _
    onRestartBodies += el
    el
  }

  override protected def onActivityResult(requestCode: Int, resultCode: Int, data: Intent) = {
    super.onActivityResult(requestCode, resultCode, data)
    onActivityResultBodies.foreach(_(requestCode, resultCode, data))
  }

  protected val onActivityResultBodies = new ArrayBuffer[(Int, Int, Intent) => Any]

  protected def onActivityResult(body: (Int, Int, Intent) => Any) = {
    onActivityResultBodies += body
    body
  }
}
