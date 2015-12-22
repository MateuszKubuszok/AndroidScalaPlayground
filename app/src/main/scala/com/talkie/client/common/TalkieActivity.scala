package com.talkie.client.common

import android.app.Activity
import android.util.Log

trait TalkieActivity extends Activity with ActivityViews with NavigationUtils {

  private val TAG = this.getClass.getSimpleName

  object logger {
    def assert(msg: String) = Log.wtf(TAG, msg)
    def trace(msg: String) = Log.v(TAG, msg)
    def info(msg: String) = Log.i(TAG, msg)
    def debug(msg: String) = Log.d(TAG, msg)
    def warn(msg: String) = Log.w(TAG, msg)
    def error(msg: String) = Log.e(TAG, msg)
  }
}
