package com.talkie.client.views.discovering

import android.view.{ Menu, MenuItem }
import com.talkie.client.views.discovering.DrawerOptions.DrawerOption
import com.talkie.client.views.discovering.MenuOptions.MenuOption

import scalaz.{ :<:, Free }

sealed trait DiscoveringViewsService[R]

object DiscoveringViewsService {

  case object InitializeLayout extends DiscoveringViewsService[Unit]
  case class InitializeMenu(menu: Menu) extends DiscoveringViewsService[Unit]
  case object CloseDrawerIfOpened extends DiscoveringViewsService[Unit]
  case class ItemToDrawerOption(item: MenuItem) extends DiscoveringViewsService[DrawerOption]
  case class ItemToMenuOption(item: MenuItem) extends DiscoveringViewsService[MenuOption]

  class Ops[S[_]](implicit s0: DiscoveringViewsService :<: S) {

    def initializeLayout: Free[S, Unit] =
      Free.liftF(s0.inj(InitializeLayout))

    def initializeMenu(menu: Menu): Free[S, Unit] =
      Free.liftF(s0.inj(InitializeMenu(menu)))

    def closeDrawerIfOpened: Free[S, Unit] =
      Free.liftF(s0.inj(CloseDrawerIfOpened))

    def itemToDrawerOption(item: MenuItem): Free[S, DrawerOption] =
      Free.liftF(s0.inj(ItemToDrawerOption(item)))

    def itemToMenuOption(item: MenuItem): Free[S, MenuOption] =
      Free.liftF(s0.inj(ItemToMenuOption(item)))
  }
}
