package com.talkie.client.app.activities.common

import android.content.Context
import com.facebook.login.widget.LoginButton
import com.talkie.client.app.services.Services
import com.talkie.client.common.components.Activity
import com.talkie.client.common.logging.Logger
import com.talkie.client.views.TypedFindView
import com.talkie.client.views.common.views.TypedFindLayout
import net.danlew.android.joda.JodaTimeAndroid

import scalaz._

trait Controller extends TypedFindView with TypedFindLayout { self: Activity =>

  import Services.Eff
  import Services.EffServices._

  protected val logger: Logger
  protected def loginButtonOpt(): Option[LoginButton] = None

  final def initializeController(androidContext: Context): Free[Eff, Unit] = for {
    _ <- configureAndroid(androidContext)
    //    _ <- configureTracking
    _ <- navigationService.configureNavigation
    _ <- facebookService.configureLogin(self, loginButtonOpt)
  } yield logger.debug("Controller initialized")

  private def configureAndroid(androidContext: Context): Free[Eff, Unit] =
    Free.point {
      JodaTimeAndroid.init(androidContext) // TODO: remember what requires this (Slick?) and move it out of here
    }

  protected def moveToLoginActivityIfLoggedOut: Free[Eff, Unit] = for {
    isLogged <- facebookService.checkIfLoggedToFacebook
    _ <- {
      if (isLogged || loginButtonOpt().isDefined) doNothing
      else navigationService.moveToLogin
    }
  } yield logger.trace("Ensured that app is not on invalid activity")
}
