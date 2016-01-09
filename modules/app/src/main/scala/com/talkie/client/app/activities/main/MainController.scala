package com.talkie.client.app.activities.main

import com.talkie.client.app.activities.common.{ Controller, RichActivity }
import com.talkie.client.app.navigation.{ AccessTokenObserver, AuthNavigationComponent }

trait MainController extends Controller {
  self: RichActivity with AccessTokenObserver with AuthNavigationComponent with MainViews =>

  onStart {
    asyncAction {
      authNavigation.moveToMainIfLogged()
    }
  }

  onRestart {
    asyncAction {
      authNavigation.moveToMainIfLogged()
    }
  }
}
