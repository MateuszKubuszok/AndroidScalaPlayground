package com.talkie.client.app.activities.login

import com.talkie.client.app.activities.common.Controller
import com.talkie.client.app.navigation.Service._
import com.talkie.client.common.services.Service._
import com.talkie.client.core.facebook.Service._
import com.talkie.client.views.login.{ Service => Views }

trait LoginController extends Controller {

  final def initializeLayout = Views.initializeLayout

  final def getLoginButton = Views.getLoginButton

  final def moveToDiscoveringActivityIfLoggedIn = for {
    isLogged <- checkIfLoggedToFacebook
    _ <- {
      if (isLogged) moveToDiscovering
      else doNothing
    }
  } yield ()
}
