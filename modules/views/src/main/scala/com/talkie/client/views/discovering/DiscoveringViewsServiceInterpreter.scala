package com.talkie.client.views.discovering

import scalaz.~>
import scalaz.concurrent.Task

final class DiscoveringViewsServiceTaskInterpreter(
    implicit
    discoveringViewsActions: DiscoveringViewsActions
) extends (DiscoveringViewsService ~> Task) {

  import DiscoveringViewsService._

  override def apply[T](in: DiscoveringViewsService[T]): Task[T] = in match {

    case InitializeLayout => Task {
      discoveringViewsActions.initializeLayout().asInstanceOf[T]
    }

    case InitializeMenu(menu) => Task {
      discoveringViewsActions.initializeMenu(menu).asInstanceOf[T]
    }

    case CloseDrawerIfOpened => Task {
      discoveringViewsActions.closeDrawerIfOpened().asInstanceOf[T]
    }

    case ItemToDrawerOption(item) => Task {
      discoveringViewsActions.itemToDrawerOption(item).asInstanceOf[T]
    }

    case ItemToMenuOption(item) => Task {
      discoveringViewsActions.itemToMenuOption(item).asInstanceOf[T]
    }
  }
}
