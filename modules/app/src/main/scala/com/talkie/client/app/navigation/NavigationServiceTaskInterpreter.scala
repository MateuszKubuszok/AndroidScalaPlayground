package com.talkie.client.app.navigation

import scalaz.~>
import scalaz.concurrent.Task

final class NavigationServiceTaskInterpreter(implicit navigationActions: NavigationActions)
    extends (NavigationService ~> Task) {

  import NavigationService._

  override def apply[R](in: NavigationService[R]): Task[R] = in match {

    case ConfigureNavigation => Task {
      navigationActions.configureNavigation().asInstanceOf[R]
    }

    case MoveToDiscovering => Task {
      navigationActions.moveToDiscovering().asInstanceOf[R]
    }

    case MoveToSettings => Task {
      navigationActions.moveToSettings().asInstanceOf[R]
    }

    case MoveToLogin => Task {
      navigationActions.moveToLogin().asInstanceOf[R]
    }

    case MoveToLoginOrElse(moveTo) => Task{
      navigationActions.moveToLoginOrElse {
        apply(moveTo).asInstanceOf[Task[Unit]]
      }.asInstanceOf[R]
    }
  }
}
