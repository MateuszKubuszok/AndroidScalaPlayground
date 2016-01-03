package com.talkie.client.app.initialization

import android.app.Activity
import com.facebook.FacebookSdk

private[initialization] trait FacebookInitializer extends Initialization {
  self: Activity =>

  override def initialize() {
    super.initialize()
    FacebookSdk.sdkInitialize(getApplicationContext)
  }
}
