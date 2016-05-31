package com.talkie.client.app.activities.main

import com.talkie.client.app.activities.common.Controller
import com.talkie.client.app.navigation.NavigationService._
import com.talkie.client.core.components.Activity
import com.talkie.client.core.services.ServiceInterpreter._
import com.talkie.client.views.R
import com.talkie.client.views.main.MainViews

trait MainController extends Controller {
  self: Activity with MainViews =>

  onCreate {
    setContentView(R.layout.activity_main)
  }

  onStart {
    moveToLoginOrElse(moveTo.discovering).fireAndForget()
  }

  onRestart {
    moveToLoginOrElse(moveTo.discovering).fireAndForget()
  }
}
