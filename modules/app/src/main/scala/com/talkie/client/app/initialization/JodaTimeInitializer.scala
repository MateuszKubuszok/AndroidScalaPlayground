package com.talkie.client.app.initialization

import com.talkie.client.core.services.Context
import com.talkie.client.views.common.RichActivity
import net.danlew.android.joda.JodaTimeAndroid

class JodaTimeInitializer(activity: RichActivity, context: Context) extends Initialization {

  def initialize() {
    JodaTimeAndroid.init(activity.getApplication)
    context.loggerFor(this) info "JodaTime initialized"
  }
}
