package com.talkie.client.core.components

import android.content.Intent

import scala.collection.mutable

trait Service extends android.app.Service {

  // lifecycle callbacks

  private val onCreateBlocks = mutable.MutableList[() => Unit]()
  protected def onCreate(block: => Unit): Unit = onCreateBlocks += (block _)
  override protected def onCreate(): Unit = {
    super.onCreate()
    onCreateBlocks.foreach { _() }
  }

  private val onStartCommandBlocks = mutable.MutableList[() => Unit]()
  protected def onStartCommand(block: => Unit): Unit = onStartCommandBlocks += (block _)
  override protected def onStartCommand(intent: Intent, flags: Int, startId: Int): Int = {
    onStartCommandBlocks.foreach { _() }
    super.onStartCommand(intent: Intent, flags: Int, startId: Int)
  }

  private val onDestroyBlocks = mutable.MutableList[() => Unit]()
  protected def onDestroy(block: => Unit): Unit = onDestroyBlocks += (block _)
  override protected def onDestroy(): Unit = {
    super.onDestroy()
    onDestroyBlocks.foreach { _() }
  }
}
