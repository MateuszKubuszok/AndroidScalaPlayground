package com.talkie.client.app.activities.discovering

import android.view.{ Menu, MenuItem }
import com.talkie.client.app.activities.common.Controller
import com.talkie.client.app.navigation.Service.moveToSettings
import com.talkie.client.core.facebook.Service.logOutFromFacebook
import com.talkie.client.views.discovering.{ MenuOptions, Service => Views }

trait DiscoveringController extends Controller {

  final def initializeLayout = Views.initializeLayout

  final def closeDrawerIfOpened = Views.closeDrawerIfOpened

  final def initializeMenu(menu: Menu) = for {
    _ <- Views.initializeMenu(menu)
  } yield ()

  final def handleDrawerOptions(item: MenuItem) = for {
    _ <- Views.itemToDrawerOption(item)
    _ <- Views.closeDrawerIfOpened
  } yield ()

  final def handleMenuOptions(item: MenuItem) = for {
    option <- Views.itemToMenuOption(item)
    _ <- option match {
      case MenuOptions.OpenSettings => moveToSettings
      case MenuOptions.Logout       => logOutFromFacebook
    }
  } yield ()
}
