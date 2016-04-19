package com.talkie.client.views.common

import android.content.Intent
import android.os.Bundle
import android.view.{ Menu, MenuItem }
import org.scaloid.common.SActivity

import scala.collection.mutable.ArrayBuffer

trait RichActivity extends SActivity {

  // onPostCreate

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

  // onRestart

  override protected def onRestart() {
    super.onRestart()
    onRestartBodies.foreach(_())
  }

  protected val onRestartBodies = new ArrayBuffer[() => Any]

  def onRestart(body: => Any) = {
    val el = body _
    onRestartBodies += el
    el
  }

  // on ActivityResult

  override protected def onActivityResult(requestCode: Int, resultCode: Int, data: Intent) = {
    super.onActivityResult(requestCode, resultCode, data)
    onActivityResultBodies.foreach(_(requestCode, resultCode, data))
  }

  protected val onActivityResultBodies = new ArrayBuffer[(Int, Int, Intent) => Any]

  protected def onActivityResult(body: (Int, Int, Intent) => Any) = {
    onActivityResultBodies += body
    body
  }

  // onBackPressed

  override protected def onBackPressed() =
    if (!(onBackPressedBodies map (_()) exists identity)) {
      super.onBackPressed()
    }

  protected val onBackPressedBodies = new ArrayBuffer[() => Boolean]

  protected def onBackPressed(body: => Boolean) = {
    val el = body _
    onBackPressedBodies += el
    el
  }

  // onCreateOptionsMenu

  override protected def onCreateOptionsMenu(menu: Menu) =
    (onCreateOptionsMenuBodies map (_(menu)) exists identity) || super.onCreateOptionsMenu(menu)

  protected val onCreateOptionsMenuBodies = new ArrayBuffer[Menu => Boolean]

  protected def onCreateOptionsMenu(body: Menu => Boolean) = {
    onCreateOptionsMenuBodies += body
    body
  }

  // onOptionsItemSelected

  override protected def onOptionsItemSelected(item: MenuItem) =
    (onOptionsItemSelectedBodies map (_(item)) exists identity) || super.onOptionsItemSelected(item)

  protected val onOptionsItemSelectedBodies = new ArrayBuffer[MenuItem => Boolean]

  protected def onOptionsItemSelected(body: MenuItem => Boolean) = {
    onOptionsItemSelectedBodies += body
    body
  }
}
