package com.talkie.client.core.components

import android.content.Intent
import android.os.Bundle
import android.view.{ MenuItem, Menu }
import com.talkie.client.core.logging.Logger

import scala.collection.mutable

trait Activity extends android.app.Activity {

  protected val logger: Logger

  // lifecycle callbacks

  private val onCreateBlocks = mutable.MutableList[Option[Bundle] => Unit]()
  def onCreate(block: => Unit): Unit = onCreateBlocks += (_ => block)
  def onCreate(block: Option[Bundle] => Unit): Unit = onCreateBlocks += block
  override protected def onCreate(savedInstanceState: Bundle): Unit = {
    logger debug s"Running onCreate actions (${onCreateBlocks.size})"
    super.onCreate(savedInstanceState)
    val savedInstanceStateOpt = Option(savedInstanceState)
    onCreateBlocks.foreach { _(savedInstanceStateOpt) }
  }

  private val onStartBlocks = mutable.MutableList[() => Unit]()
  def onStart(block: => Unit): Unit = onStartBlocks += (block _)
  override protected def onStart(): Unit = {
    logger debug s"Running onStart actions (${onStartBlocks.size})"
    super.onStart()
    onStartBlocks.foreach { _() }
  }

  private val onPostCreateBlocks = mutable.MutableList[Option[Bundle] => Unit]()
  def onPostCreate(block: => Unit): Unit = onPostCreateBlocks += (_ => block)
  def onPostCreate(block: Option[Bundle] => Unit): Unit = onPostCreateBlocks += block
  override protected def onPostCreate(savedInstanceState: Bundle): Unit = {
    logger debug s"Running onPostCreate actions (${onPostCreateBlocks.size})"
    super.onPostCreate(savedInstanceState)
    val savedInstanceStateOpt = Option(savedInstanceState)
    onPostCreateBlocks.foreach { _(savedInstanceStateOpt) }
  }

  private val onPauseBlocks = mutable.MutableList[() => Unit]()
  def onPause(block: => Unit): Unit = onPauseBlocks += (block _)
  override protected def onPause(): Unit = {
    logger debug s"Running onPause actions (${onPauseBlocks.size})"
    super.onPause()
    onPauseBlocks.foreach { _() }
  }

  private val onResumeBlocks = mutable.MutableList[() => Unit]()
  def onResume(block: => Unit): Unit = onResumeBlocks += (block _)
  override protected def onResume(): Unit = {
    logger debug s"Running onResume actions (${onResumeBlocks.size})"
    super.onResume()
    onResumeBlocks.foreach { _() }
  }

  private val onStopBlocks = mutable.MutableList[() => Unit]()
  def onStop(block: => Unit): Unit = onStopBlocks += (block _)
  override protected def onStop(): Unit = {
    logger debug s"Running onStop actions (${onCreateBlocks.size})"
    super.onStop()
    onStopBlocks.foreach { _() }
  }

  private val onRestartBlocks = mutable.MutableList[() => Unit]()
  def onRestart(block: => Unit): Unit = onRestartBlocks += (block _)
  override protected def onRestart(): Unit = {
    logger debug s"Running onRestart actions (${onRestartBlocks.size})"
    super.onRestart()
    onRestartBlocks.foreach { _() }
  }

  private val onPostResumeBlocks = mutable.MutableList[() => Unit]()
  def onPostResume(block: => Unit): Unit = onPostResumeBlocks += (block _)
  override protected def onPostResume(): Unit = {
    logger debug s"Running onPostResume actions (${onPostResumeBlocks.size})"
    super.onPostResume()
    onPostResumeBlocks.foreach { _() }
  }

  private val onDestroyBlocks = mutable.MutableList[() => Unit]()
  def onDestroy(block: => Unit): Unit = onDestroyBlocks += (block _)
  override protected def onDestroy(): Unit = {
    logger debug s"Running onDestroy actions (${onDestroyBlocks.size})"
    super.onDestroy()
    onDestroyBlocks.foreach { _() }
  }

  // initialization/release

  def bootstrap(block: => Unit): Unit = {
    onPostCreate(block)
  }

  def teardown(block: => Unit): Unit = {
    onDestroy(block)
  }

  // action callbacks

  private val onActivityResultBlocks = mutable.MutableList[(Int, Int, Intent) => Unit]()
  def onActivityResult(block: => Unit): Unit = onActivityResultBlocks += ((_, _, _) => block)
  def onActivityResult(block: (Int, Int, Intent) => Unit): Unit = onActivityResultBlocks += block
  override protected def onActivityResult(requestCode: Int, resultCode: Int, data: Intent): Unit = {
    logger debug s"Running onActivityResult actions (${onActivityResultBlocks.size})"
    super.onActivityResult(requestCode, resultCode, data)
    onActivityResultBlocks.foreach { _(requestCode, resultCode, data) }
  }

  private val onBackPressedBlocks = mutable.MutableList[() => Unit]()
  def onBackPressed(block: => Unit): Unit = onBackPressedBlocks += (block _)
  override protected def onBackPressed(): Unit = {
    logger debug s"Running onBackPressed actions (${onBackPressedBlocks.size})"
    super.onBackPressed()
    onBackPressedBlocks.foreach { _() }
  }

  private val onCreateOptionsMenuBlocks = mutable.MutableList[Menu => Boolean]()
  def onCreateOptionsMenu(block: => Boolean): Unit = onCreateOptionsMenuBlocks += (_ => block)
  def onCreateOptionsMenu(block: Menu => Boolean): Unit = onCreateOptionsMenuBlocks += block
  override protected def onCreateOptionsMenu(menu: Menu): Boolean = {
    logger debug s"Running onCreateOptionsMenu actions (${onCreateOptionsMenuBlocks.size})"
    super.onCreateOptionsMenu(menu) && onCreateOptionsMenuBlocks.forall { _(menu) }
  }

  private val onOptionsItemSelectedBlocks = mutable.MutableList[MenuItem => Boolean]()
  def onOptionsItemSelected(block: => Boolean): Unit = onOptionsItemSelectedBlocks += (_ => block)
  def onOptionsItemSelected(block: MenuItem => Boolean): Unit = onOptionsItemSelectedBlocks += block
  override protected def onOptionsItemSelected(item: MenuItem): Boolean = {
    logger debug s"Running onOptionsItemSelected actions (${onOptionsItemSelectedBlocks.size})"
    onOptionsItemSelectedBlocks.exists { _(item) } || super.onOptionsItemSelected(item)
  }
}
