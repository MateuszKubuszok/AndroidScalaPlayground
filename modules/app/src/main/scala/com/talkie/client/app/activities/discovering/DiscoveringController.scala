package com.talkie.client.app.activities.discovering

import android.view.{ Menu, MenuItem }
import com.talkie.client.app.activities.common.Controller
import com.talkie.client.app.navigation.NavigationService
import com.talkie.client.common.components.Activity
import com.talkie.client.core.facebook.FacebookService
import com.talkie.client.views.discovering._

import scalaz._

trait DiscoveringController extends Controller { self: Activity =>

  type S0[A] = FacebookService[A]
  type S1[A] = Coproduct[NavigationService, S0, A]
  type S2[A] = Coproduct[DiscoveringViewsService, S1, A]

  type Eff[A] = S2[A]

  private class ProvedServices[S[_]](
      implicit
      s0: FacebookService :<: S,
      s1: NavigationService :<: S,
      s2: DiscoveringViewsService :<: S
  ) {

    val facebookService = new FacebookService.Ops[S]
    val navigationService = new NavigationService.Ops[S]
    val views = new DiscoveringViewsService.Ops[S]
  }
  private object ProvedServices extends ProvedServices[Eff]
  import ProvedServices._

  final def initializeLayout = views.initializeLayout

  final def closeDrawerIfOpened = views.closeDrawerIfOpened

  final def initializeMenu(menu: Menu) = views.initializeMenu(menu)

  final def handleDrawerOptions(item: MenuItem) = for {
    _ <- views.itemToDrawerOption(item)
    _ <- views.closeDrawerIfOpened
  } yield ()

  final def handleMenuOptions(item: MenuItem) = for {
    option <- views.itemToMenuOption(item)
    _ <- option match {
      case MenuOptions.OpenSettings => navigationService.moveToSettings
      case MenuOptions.Logout       => facebookService.logOutFromFacebook
    }
  } yield ()
}
