package com.talkie.client.app.initialization

import com.facebook.FacebookSdk
import com.talkie.client.app.activities.common.RichActivity
import com.talkie.client.core.logging.LoggerComponent

private[initialization] trait FacebookInitializer extends Initialization {
  self: RichActivity with LoggerComponent =>

  onInitialization {
    FacebookSdk.sdkInitialize(getApplicationContext)
    logger info "Facebook SDK initialized"
  }
}
