package com.talkie.client.app.activities.main

import com.talkie.client.app.activities.common.Controller
import com.talkie.client.views.R
import com.talkie.client.views.common.RichActivity
import com.talkie.client.views.main.MainViews

trait MainController extends Controller {
  self: RichActivity with MainViews =>

  implicit val ec = context.serviceExecutionContext

  onCreate {
    setContentView(R.layout.activity_main)
  }

  onStart {
    asyncAction {
      authNavigationF map (_.moveDependingOnLoginState())
    }
  }

  onRestart {
    asyncAction {
      authNavigationF map (_.moveDependingOnLoginState())
    }
  }
}
