package com.talkie.client.activities.init

import android.app.Activity
import com.talkie.client.R
import com.talkie.client.activities.common.Controller
import com.talkie.client.navigation.AutomatedAuthNavigation

trait InitController extends Controller {
  self: Activity
    with AutomatedAuthNavigation =>

  final protected def onCreateEvent() {
    setContentView(R.layout.activity_init)
    logger trace "Initial activity initialized"
  }

  final protected def onStartEvent() = asyncAction {
    moveToMainIfLogged()
  }

  final protected def onRestartEvent() = asyncAction {
    moveToMainIfLogged()
  }
}
