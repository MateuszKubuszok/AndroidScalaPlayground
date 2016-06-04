package com.talkie.client.app.navigation

import com.talkie.client.common.components.Activity
import com.talkie.client.common.context.Context
import com.talkie.client.common.services.{ ~@~>, ~&~> }
import com.talkie.client.core.events.EventServiceInterpreter
import com.talkie.client.core.facebook.FacebookServiceInterpreter

import scalaz.concurrent.Task

trait NavigationServiceInterpreter extends (NavigationService ~@~> Task)

object NavigationServiceInterpreter extends (NavigationService ~&~> Task)

final class NavigationServiceInterpreterImpl(
    context:    Context,
    activity:   Activity,
    eventSI:    EventServiceInterpreter,
    facebookSI: FacebookServiceInterpreter
) extends NavigationServiceInterpreter {

  private val actions = new NavigationActions(context, activity, eventSI, facebookSI)

  override def apply[R](in: NavigationService[R]): Task[R] = in match {

    case ConfigureNavigation => Task {
      actions.configureNavigation().asInstanceOf[R]
    }

    case MoveToDiscovering => Task {
      actions.moveToDiscovering().asInstanceOf[R]
    }

    case MoveToSettings => Task {
      actions.moveToSettings().asInstanceOf[R]
    }

    case MoveToLogin => Task {
      actions.moveToLogin().asInstanceOf[R]
    }

    case MoveToLoginOrElse(moveTo) => actions.moveToLoginOrElse {
      apply(moveTo).asInstanceOf[Task[Unit]]
    }.asInstanceOf[Task[R]]
  }
}
