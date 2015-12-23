package com.talkie.client.services

import android.util.Log

trait Logger {

  def assertionFailed(msg: String): Unit
  def trace(msg: String): Unit
  def info(msg: String): Unit
  def debug(msg: String): Unit
  def warn(msg: String): Unit
  def error(msg: String): Unit
}

trait LoggerComponent {

  def logger: Logger
}

trait LoggerComponentImpl extends LoggerComponent {

  private val TAG = this.getClass.getSimpleName

  object logger extends Logger {

    def assertionFailed(msg: String) = Log.wtf(TAG, msg)
    def trace(msg: String) = Log.v(TAG, msg)
    def info(msg: String) = Log.i(TAG, msg)
    def debug(msg: String) = Log.d(TAG, msg)
    def warn(msg: String) = Log.w(TAG, msg)
    def error(msg: String) = Log.e(TAG, msg)
  }
}
