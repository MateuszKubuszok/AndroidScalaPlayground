package com.talkie.client.core.services

import java.util.concurrent.TimeUnit.SECONDS
import java.util.concurrent.{ SynchronousQueue, ThreadPoolExecutor }

import android.os.Looper
import com.talkie.client.core.logging.LoggerImpl

import scala.collection.mutable

case object ServiceExecutor extends ThreadPoolExecutor(8, 16, 30, SECONDS, new SynchronousQueue[Runnable]()) {

  private val logger = new LoggerImpl(this.getClass.getSimpleName)

  val initialized = mutable.WeakHashMap.empty[Thread, Boolean]

  override protected def beforeExecute(t: Thread, r: Runnable) = {
    super.beforeExecute(t, r)
    if (!initialized.contains(t)) {
      initialized.put(t, true)
      Looper.prepare()
    }
  }

  override protected def afterExecute(r: Runnable, t: Throwable): Unit = {
    super.afterExecute(r, t)
    Option(t) foreach { throwable =>
      logger error ("Unhandled exception within ExecutionContext", t)
    }
  }
}
