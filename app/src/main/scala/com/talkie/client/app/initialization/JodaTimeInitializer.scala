package com.talkie.client.app.initialization

import android.app.Activity
import net.danlew.android.joda.JodaTimeAndroid

private[initialization] trait JodaTimeInitializer extends Initialization {
  self: Activity =>

  override def initialize() {
    super.initialize()
    JodaTimeAndroid.init(getApplication)
  }
}
