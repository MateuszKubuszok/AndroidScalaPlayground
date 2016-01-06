package com.talkie.client.app.initialization

import com.talkie.client.app.activities.common.RichActivity
import com.talkie.client.core.logging.LoggerComponent
import net.danlew.android.joda.JodaTimeAndroid

private[initialization] trait JodaTimeInitializer extends Initialization {
  self: RichActivity with LoggerComponent =>

  onInitialization {
    JodaTimeAndroid.init(getApplication)
    logger info "JodaTime initialized"
  }
}
