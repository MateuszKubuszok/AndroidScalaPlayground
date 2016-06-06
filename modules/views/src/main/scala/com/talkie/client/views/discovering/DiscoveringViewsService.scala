package com.talkie.client.views.discovering

import android.view.{ Menu, MenuItem }
import com.talkie.client.common.services.{ Service => GenericService }
import com.talkie.client.views.discovering.DrawerOptions.DrawerOption
import com.talkie.client.views.discovering.MenuOptions.MenuOption

import scalaz.Free

sealed trait DiscoveringViewsService[R] extends GenericService[R]
case object InitializeLayout extends DiscoveringViewsService[Unit]
case class InitializeMenu(menu: Menu) extends DiscoveringViewsService[Unit]
case object CloseDrawerIfOpened extends DiscoveringViewsService[Unit]
case class ItemToDrawerOption(item: MenuItem) extends DiscoveringViewsService[DrawerOption]
case class ItemToMenuOption(item: MenuItem) extends DiscoveringViewsService[MenuOption]

trait DiscoveringViewsServiceFrees[S[R] >: DiscoveringViewsService[R]] {

  def initializeLayout: Free[S, Unit] =
    Free.liftF(InitializeLayout: S[Unit])

  def initializeMenu(menu: Menu): Free[S, Unit] =
    Free.liftF(InitializeMenu(menu): S[Unit])

  def closeDrawerIfOpened: Free[S, Unit] =
    Free.liftF(CloseDrawerIfOpened: S[Unit])

  def itemToDrawerOption(item: MenuItem): Free[S, DrawerOption] =
    Free.liftF(ItemToDrawerOption(item): S[DrawerOption])

  def itemToMenuOption(item: MenuItem): Free[S, MenuOption] =
    Free.liftF(ItemToMenuOption(item): S[MenuOption])
}

object FacebookService extends DiscoveringViewsServiceFrees[DiscoveringViewsService]
object Service extends DiscoveringViewsServiceFrees[GenericService]
