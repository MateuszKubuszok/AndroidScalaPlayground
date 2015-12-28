package com.talkie.client.core.initialization

import android.app.Activity
import com.facebook.FacebookSdk

trait FacebookInitializer extends Initialization {
  self: Activity =>

  override def initialize() {
    super.initialize()
    FacebookSdk.sdkInitialize(getApplicationContext)
  }
}
