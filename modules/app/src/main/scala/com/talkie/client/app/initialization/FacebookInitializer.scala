package com.talkie.client.app.initialization

import android.app.Activity
import com.facebook.FacebookSdk
import com.talkie.client.core.logging.LoggerComponent

private[initialization] trait FacebookInitializer extends Initialization {
  self: Activity with LoggerComponent =>

  override def initialize() {
    super.initialize()
    FacebookSdk.sdkInitialize(getApplicationContext)
    logger info "Facebook SDK initialized"
  }
}
