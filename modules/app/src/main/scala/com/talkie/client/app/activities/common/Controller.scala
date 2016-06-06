package com.talkie.client.app.activities.common

import android.content.Context
import com.facebook.login.widget.LoginButton
import com.talkie.client.app.navigation.Service._
import com.talkie.client.common.logging.Logger
import com.talkie.client.common.services.Service
import com.talkie.client.common.services.Service._
import com.talkie.client.core.facebook.Service._
//import com.talkie.client.domain.tracking.Service._
import com.talkie.client.views.TypedFindView
import com.talkie.client.views.common.views.TypedFindLayout
import net.danlew.android.joda.JodaTimeAndroid

import scalaz.Free

trait Controller extends TypedFindView with TypedFindLayout {

  protected val logger: Logger
  protected def loginButtonOpt(): Option[LoginButton] = None

  final def initializeController(androidContext: Context) = for {
    _ <- configureAndroid(androidContext)
    //    _ <- configureTracking
    _ <- configureNavigation
    _ <- configureLogin(loginButtonOpt)
  } yield logger.debug("Controller initialized")

  private def configureAndroid(androidContext: Context): Free[Service, Unit] =
    Free.point {
      JodaTimeAndroid.init(androidContext) // TODO: remember what requires this (Slick?) and move it out of here
    }

  protected def moveToLoginActivityIfLoggedOut = for {
    isLogged <- checkIfLoggedToFacebook
    _ <- {
      if (isLogged || loginButtonOpt().isDefined) doNothing
      else moveToLogin
    }
  } yield logger.trace("Ensured that app is not on invalid activity")
}
