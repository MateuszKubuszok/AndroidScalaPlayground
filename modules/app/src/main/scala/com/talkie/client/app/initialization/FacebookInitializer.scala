package com.talkie.client.app.initialization

import com.facebook.FacebookSdk
import com.talkie.client.core.services.Context
import com.talkie.client.views.common.RichActivity

class FacebookInitializer(activity: RichActivity, context: Context) extends Initialization {

  def initialize() {
    FacebookSdk.sdkInitialize(activity.getApplicationContext)
    context.loggerFor(this) info "Facebook SDK initialized"
  }
}
