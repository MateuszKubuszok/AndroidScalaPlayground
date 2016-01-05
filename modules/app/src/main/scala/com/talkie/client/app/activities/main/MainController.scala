package com.talkie.client.app.activities.main

import android.app.Activity
import com.talkie.client.R
import com.talkie.client.app.activities.common.Controller
import com.talkie.client.app.navigation.{ AccessTokenObserver, AuthNavigationComponent }

trait MainController extends Controller {
  self: Activity with AccessTokenObserver with AuthNavigationComponent =>

  final protected def onCreateEvent() {
    setContentView(R.layout.activity_main)
    logger trace "Initial activity initialized"
  }

  final protected def onStartEvent() = asyncAction {
    authNavigation.moveToMainIfLogged()
  }

  final protected def onRestartEvent() = asyncAction {
    authNavigation.moveToMainIfLogged()
  }
}
