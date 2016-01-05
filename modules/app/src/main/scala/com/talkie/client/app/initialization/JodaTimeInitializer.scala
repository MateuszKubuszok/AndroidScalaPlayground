package com.talkie.client.app.initialization

import android.app.Activity
import com.talkie.client.core.logging.LoggerComponent
import net.danlew.android.joda.JodaTimeAndroid

private[initialization] trait JodaTimeInitializer extends Initialization {
  self: Activity with LoggerComponent =>

  override def initialize() {
    super.initialize()
    JodaTimeAndroid.init(getApplication)
    logger info "JodaTime initialized"
  }
}
