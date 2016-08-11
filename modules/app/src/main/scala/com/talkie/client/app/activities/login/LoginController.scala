package com.talkie.client.app.activities.login

import com.talkie.client.app.activities.common.Controller
import com.talkie.client.app.navigation.NavigationService
import com.talkie.client.common.components.Activity
import com.talkie.client.core.facebook.FacebookService
import com.talkie.client.views.login.LoginViewsService

import scalaz._

trait LoginController extends Controller { self: Activity =>

  type S0[A] = FacebookService[A]
  type S1[A] = Coproduct[NavigationService, S0, A]
  type S2[A] = Coproduct[LoginViewsService, S1, A]

  type Eff[A] = S2[A]

  private class ProvedServices[S[_]](
      implicit
      s0: FacebookService :<: S,
      s1: NavigationService :<: S,
      s2: LoginViewsService :<: S
  ) {

    val facebookService = new FacebookService.Ops[S]
    val navigationService = new NavigationService.Ops[S]
    val views = new LoginViewsService.Ops[S]
    val doNothing = Free.point[S, Unit](())
  }
  private object ProvedServices extends ProvedServices[Eff]
  import ProvedServices._

  final def initializeLayout = views.initializeLayout

  final def getLoginButton = views.getLoginButton

  final def moveToDiscoveringActivityIfLoggedIn = for {
    isLogged <- facebookService.checkIfLoggedToFacebook
    _ <- {
      if (isLogged) navigationService.moveToDiscovering
      else doNothing
    }
  } yield ()
}
