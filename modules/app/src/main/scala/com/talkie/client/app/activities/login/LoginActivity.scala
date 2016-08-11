package com.talkie.client.app.activities.login

import com.talkie.client.app.activities.common.AppActivity
import com.talkie.client.app.navigation.NavigationServiceTaskInterpreter
import com.talkie.client.common.services.EnrichNTOps._
import com.talkie.client.core.facebook.FacebookServiceTaskInterpreter
import com.talkie.client.views.login._

import scalaz._
import scalaz.concurrent.Task

final class LoginActivity extends AppActivity with LoginController {

  implicit val loginViews: LoginViews = new LoginViewsImpl
  implicit val loginViewsActions: LoginViewsActions = new LoginViewsActionsImpl

  val i0: S0 ~> Task = new FacebookServiceTaskInterpreter
  val i1: S1 ~> Task = new NavigationServiceTaskInterpreter :+: i0
  val i2: S2 ~> Task = new LoginViewsServiceTaskInterpreter :+: i1

  val interpreter: Eff ~> Task = i2

  override def loginButtonOpt() = getLoginButton.foldMap(interpreter).unsafePerformSyncAttempt.getOrElse(None)

  onCreate { _ =>
    initializeLayout.foldMap(interpreter).unsafePerformSync
  }

  onStart {
    moveToDiscoveringActivityIfLoggedIn.foldMap(interpreter).unsafePerformAsync(_ => ())
  }

  onResume {
    moveToDiscoveringActivityIfLoggedIn.foldMap(interpreter).unsafePerformAsync(_ => ())
  }
}
