package com.talkie.client.app.navigation

import android.content.Intent
import com.talkie.client.app.activities.discovering.DiscoveringActivity
import com.talkie.client.app.activities.login.LoginActivity
import com.talkie.client.app.activities.settings.SettingsActivity
import com.talkie.client.core.components.Activity
import com.talkie.client.core.context.Context
import com.talkie.client.core.events.EventService._
import com.talkie.client.core.events.{ EventListener, EventServiceInterpreter }
import com.talkie.client.core.facebook._
import com.talkie.client.core.facebook.FacebookService._
import com.talkie.client.core.services.{ ~@~>, ~&~> }
import com.talkie.client.core.services.ServiceInterpreter.TaskRunner

import scala.reflect.ClassTag
import scalaz.concurrent.Task

trait NavigationServiceInterpreter extends (NavigationService ~@~> Task)

object NavigationServiceInterpreter extends (NavigationService ~&~> Task)

final class NavigationServiceInterpreterImpl(
    context:                 Context,
    activity:                Activity,
    implicit val eventSI:    EventServiceInterpreter,
    implicit val facebookSI: FacebookServiceInterpreter
) extends NavigationServiceInterpreter {

  private val logger = context loggerFor this

  override def apply[R](in: NavigationService[R]): Task[R] = in match {

    case ConfigureNavigation => Task {
      configureNavigation().asInstanceOf[R]
    }

    case MoveToDiscovering => Task {
      moveToDiscovering().asInstanceOf[R]
    }

    case MoveToSettings => Task {
      moveToSettings().asInstanceOf[R]
    }

    case MoveToLogin => Task {
      moveToLogin().asInstanceOf[R]
    }

    case MoveToLoginOrElse(moveTo) => moveToLoginOrElse(moveTo).asInstanceOf[Task[R]]
  }

  private object OnUserLoggedIn extends EventListener[LoginSucceeded] {

    override def handleEvent(event: LoginSucceeded) = {
      Task(moveToDiscovering()).fireAndForget()
      true
    }
  }

  private object OnUserLoggedOut extends EventListener[LoggedOut] {

    override def handleEvent(event: LoggedOut) = {
      Task(moveToLogin()).fireAndForget()
      true
    }
  }

  private def configureNavigation(): Unit = {
    import com.talkie.client.core.events.EventServiceInterpreter._

    activity.bootstrap {
      (for {
        _ <- registerEventListener(OnUserLoggedIn)
        _ <- registerEventListener(OnUserLoggedOut)
      } yield ()).fireAndForget()
    }

    activity.teardown {
      (for {
        _ <- removeEventListener(OnUserLoggedIn)
        _ <- removeEventListener(OnUserLoggedOut)
      } yield ()).fireAndForget()
    }
  }

  private def moveToDiscovering(): Unit = startActivity[DiscoveringActivity]

  private def moveToSettings(): Unit = startActivity[SettingsActivity]

  private def moveToLogin(): Unit = startActivity[LoginActivity]

  private def moveToLoginOrElse[R](moveTo: MoveTo[R]): Task[Unit] = {
    import com.talkie.client.core.facebook.FacebookServiceInterpreter._

    checkIfLoggedToFacebook.interpret.flatMap { isLogged =>
      logger trace s"User logged?: $isLogged"
      if (isLogged) apply(moveTo)
      else Task(moveToLogin())
    }.map { _ => () }
  }

  private def startActivity[A <: Activity](implicit classTag: ClassTag[A]): Unit = {
    logger trace s"Moving to ${classTag.toString}"
    context.androidContext.startActivity(new Intent(context.androidContext, classTag.runtimeClass))
  }
}
