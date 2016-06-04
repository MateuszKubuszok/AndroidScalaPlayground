package com.talkie.client.core.components

import android.content.Intent
import android.os.Bundle
import android.view.{ MenuItem, Menu }
import com.talkie.client.core.context.{ ContextImpl, Context }
import com.talkie.client.core.logging.Logger
import com.talkie.client.core.services.ServiceInterpreter

import scala.collection.mutable

trait Activity extends android.app.Activity {

  protected val context: Context
  protected val logger: Logger
  protected implicit val serviceInterpreter: ServiceInterpreter

  // lifecycle callbacks

  def onCreate(block: => Unit): Unit
  def onCreate(block: Option[Bundle] => Unit): Unit

  def onStart(block: => Unit): Unit

  def onPostCreate(block: => Unit): Unit
  def onPostCreate(block: Option[Bundle] => Unit): Unit

  def onPause(block: => Unit): Unit

  def onResume(block: => Unit): Unit

  def onStop(block: => Unit): Unit

  def onRestart(block: => Unit): Unit

  def onPostResume(block: => Unit): Unit

  def onDestroy(block: => Unit): Unit

  // initialization/release

  def bootstrap(block: => Unit): Unit

  def teardown(block: => Unit): Unit

  // action callbacks

  def onActivityResult(block: => Unit): Unit
  def onActivityResult(block: (Int, Int, Intent) => Unit): Unit

  def onBackPressed(block: => Unit): Unit

  def onCreateOptionsMenu(block: => Boolean): Unit
  def onCreateOptionsMenu(block: Menu => Boolean): Unit

  def onOptionsItemSelected(block: => Boolean): Unit
  def onOptionsItemSelected(block: MenuItem => Boolean): Unit
}

trait ActivityImpl extends Activity {

  override protected val context = ContextImpl(this)
  override protected val logger = context.loggerFor(this)

  // lifecycle callbacks

  private val onCreateBlocks = mutable.MutableList[Option[Bundle] => Unit]()
  override def onCreate(block: => Unit): Unit = onCreateBlocks += (_ => block)
  override def onCreate(block: Option[Bundle] => Unit): Unit = onCreateBlocks += block
  override protected def onCreate(savedInstanceState: Bundle): Unit = {
    logger debug s"Running onCreate actions (${onCreateBlocks.size})"
    super.onCreate(savedInstanceState)
    val savedInstanceStateOpt = Option(savedInstanceState)
    onCreateBlocks.foreach { _(savedInstanceStateOpt) }
  }

  private val onStartBlocks = mutable.MutableList[() => Unit]()
  override def onStart(block: => Unit): Unit = onStartBlocks += (block _)
  override protected def onStart(): Unit = {
    logger debug s"Running onStart actions (${onStartBlocks.size})"
    super.onStart()
    onStartBlocks.foreach { _() }
  }

  private val onPostCreateBlocks = mutable.MutableList[Option[Bundle] => Unit]()
  override def onPostCreate(block: => Unit): Unit = onPostCreateBlocks += (_ => block)
  override def onPostCreate(block: Option[Bundle] => Unit): Unit = onPostCreateBlocks += block
  override protected def onPostCreate(savedInstanceState: Bundle): Unit = {
    logger debug s"Running onPostCreate actions (${onPostCreateBlocks.size})"
    super.onPostCreate(savedInstanceState)
    val savedInstanceStateOpt = Option(savedInstanceState)
    onPostCreateBlocks.foreach { _(savedInstanceStateOpt) }
  }

  private val onPauseBlocks = mutable.MutableList[() => Unit]()
  override def onPause(block: => Unit): Unit = onPauseBlocks += (block _)
  override protected def onPause(): Unit = {
    logger debug s"Running onPause actions (${onPauseBlocks.size})"
    super.onPause()
    onPauseBlocks.foreach { _() }
  }

  private val onResumeBlocks = mutable.MutableList[() => Unit]()
  override def onResume(block: => Unit): Unit = onResumeBlocks += (block _)
  override protected def onResume(): Unit = {
    logger debug s"Running onResume actions (${onResumeBlocks.size})"
    super.onResume()
    onResumeBlocks.foreach { _() }
  }

  private val onStopBlocks = mutable.MutableList[() => Unit]()
  override def onStop(block: => Unit): Unit = onStopBlocks += (block _)
  override protected def onStop(): Unit = {
    logger debug s"Running onStop actions (${onCreateBlocks.size})"
    super.onStop()
    onStopBlocks.foreach { _() }
  }

  private val onRestartBlocks = mutable.MutableList[() => Unit]()
  override def onRestart(block: => Unit): Unit = onRestartBlocks += (block _)
  override protected def onRestart(): Unit = {
    logger debug s"Running onRestart actions (${onRestartBlocks.size})"
    super.onRestart()
    onRestartBlocks.foreach { _() }
  }

  private val onPostResumeBlocks = mutable.MutableList[() => Unit]()
  override def onPostResume(block: => Unit): Unit = onPostResumeBlocks += (block _)
  override protected def onPostResume(): Unit = {
    logger debug s"Running onPostResume actions (${onPostResumeBlocks.size})"
    super.onPostResume()
    onPostResumeBlocks.foreach { _() }
  }

  private val onDestroyBlocks = mutable.MutableList[() => Unit]()
  override def onDestroy(block: => Unit): Unit = onDestroyBlocks += (block _)
  override protected def onDestroy(): Unit = {
    logger debug s"Running onDestroy actions (${onDestroyBlocks.size})"
    super.onDestroy()
    onDestroyBlocks.foreach { _() }
  }

  // initialization/release

  override def bootstrap(block: => Unit): Unit = {
    onPostCreate(block)
  }

  override def teardown(block: => Unit): Unit = {
    onDestroy(block)
  }

  // action callbacks

  private val onActivityResultBlocks = mutable.MutableList[(Int, Int, Intent) => Unit]()
  override def onActivityResult(block: => Unit): Unit = onActivityResultBlocks += ((_, _, _) => block)
  override def onActivityResult(block: (Int, Int, Intent) => Unit): Unit = onActivityResultBlocks += block
  override protected def onActivityResult(requestCode: Int, resultCode: Int, data: Intent): Unit = {
    logger debug s"Running onActivityResult actions (${onActivityResultBlocks.size})"
    super.onActivityResult(requestCode, resultCode, data)
    onActivityResultBlocks.foreach { _(requestCode, resultCode, data) }
  }

  private val onBackPressedBlocks = mutable.MutableList[() => Unit]()
  override def onBackPressed(block: => Unit): Unit = onBackPressedBlocks += (block _)
  override protected def onBackPressed(): Unit = {
    logger debug s"Running onBackPressed actions (${onBackPressedBlocks.size})"
    super.onBackPressed()
    onBackPressedBlocks.foreach { _() }
  }

  private val onCreateOptionsMenuBlocks = mutable.MutableList[Menu => Boolean]()
  override def onCreateOptionsMenu(block: => Boolean): Unit = onCreateOptionsMenuBlocks += (_ => block)
  override def onCreateOptionsMenu(block: Menu => Boolean): Unit = onCreateOptionsMenuBlocks += block
  override protected def onCreateOptionsMenu(menu: Menu): Boolean = {
    logger debug s"Running onCreateOptionsMenu actions (${onCreateOptionsMenuBlocks.size})"
    super.onCreateOptionsMenu(menu) && onCreateOptionsMenuBlocks.forall { _(menu) }
  }

  private val onOptionsItemSelectedBlocks = mutable.MutableList[MenuItem => Boolean]()
  override def onOptionsItemSelected(block: => Boolean): Unit = onOptionsItemSelectedBlocks += (_ => block)
  override def onOptionsItemSelected(block: MenuItem => Boolean): Unit = onOptionsItemSelectedBlocks += block
  override protected def onOptionsItemSelected(item: MenuItem): Boolean = {
    logger debug s"Running onOptionsItemSelected actions (${onOptionsItemSelectedBlocks.size})"
    onOptionsItemSelectedBlocks.exists { _(item) } || super.onOptionsItemSelected(item)
  }
}
