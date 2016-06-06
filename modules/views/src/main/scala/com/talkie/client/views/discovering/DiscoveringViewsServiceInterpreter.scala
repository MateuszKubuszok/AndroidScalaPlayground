package com.talkie.client.views.discovering

import android.support.v7.app.AppCompatActivity
import com.talkie.client.common.components.{ OnNavigationItemSelectedListener, Activity }
import com.talkie.client.common.context.Context
import com.talkie.client.common.services.{ ~@~>, ~&~> }

import scalaz.concurrent.Task

trait DiscoveringViewsServiceInterpreter extends (DiscoveringViewsService ~@~> Task)

object DiscoveringViewsServiceInterpreter extends (DiscoveringViewsService ~&~> Task)

final class DiscoveringViewsServiceInterpreterImpl(
    context:  Context,
    activity: AppCompatActivity with Activity,
    listener: OnNavigationItemSelectedListener
) extends DiscoveringViewsServiceInterpreter {

  private val actions = new DiscoveringViewsActions(context, activity, new DiscoveringViewsImpl(activity), listener)

  override def apply[T](in: DiscoveringViewsService[T]): Task[T] = in match {

    case InitializeLayout => Task {
      actions.initializeLayout().asInstanceOf[T]
    }

    case InitializeMenu(menu) => Task {
      actions.initializeMenu(menu).asInstanceOf[T]
    }

    case CloseDrawerIfOpened => Task {
      actions.closeDrawerIfOpened().asInstanceOf[T]
    }

    case ItemToDrawerOption(item) => Task {
      actions.itemToDrawerOption(item).asInstanceOf[T]
    }

    case ItemToMenuOption(item) => Task {
      actions.itemToMenuOption(item).asInstanceOf[T]
    }
  }
}
