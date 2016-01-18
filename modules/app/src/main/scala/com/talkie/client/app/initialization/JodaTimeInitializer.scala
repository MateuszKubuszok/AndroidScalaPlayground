package com.talkie.client.app.initialization

import com.talkie.client.core.logging.LoggerComponent
import com.talkie.client.views.common.RichActivity
import net.danlew.android.joda.JodaTimeAndroid

private[initialization] trait JodaTimeInitializer extends Initialization {
  self: RichActivity with LoggerComponent =>

  onInitialization {
    JodaTimeAndroid.init(getApplication)
    logger info "JodaTime initialized"
  }
}
