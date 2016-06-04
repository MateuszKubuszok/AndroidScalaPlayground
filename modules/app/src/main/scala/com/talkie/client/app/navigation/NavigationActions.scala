package com.talkie.client.app.navigation

import android.content.Intent
import com.talkie.client.app.activities.discovering.DiscoveringActivity
import com.talkie.client.app.activities.login.LoginActivity
import com.talkie.client.app.activities.settings.SettingsActivity
import com.talkie.client.common.components.Activity
import com.talkie.client.common.context.Context
import com.talkie.client.common.events.EventListener
import com.talkie.client.common.services.ServiceInterpreter
import com.talkie.client.core.events.EventService._
import com.talkie.client.core.events.EventServiceInterpreter
import com.talkie.client.core.facebook._
import com.talkie.client.core.facebook.FacebookService._
import ServiceInterpreter.TaskRunner

import scala.reflect.ClassTag
import scalaz.concurrent.Task

private[navigation] final class NavigationActions(
    context:                 Context,
    activity:                Activity,
    implicit val eventSI:    EventServiceInterpreter,
    implicit val facebookSI: FacebookServiceInterpreter
) {

  private val logger = context loggerFor this

  def configureNavigation(): Unit = {
    import com.talkie.client.core.events.EventServiceInterpreter._

    activity.bootstrap {
      (for {
        _ <- registerEventListener(OnUserLoggedIn)
        _ <- registerEventListener(OnUserLoggedOut)
      } yield ()).fireAndWait()
    }

    activity.teardown {
      (for {
        _ <- removeEventListener(OnUserLoggedIn)
        _ <- removeEventListener(OnUserLoggedOut)
      } yield ()).fireAndWait()
    }
  }

  def moveToDiscovering(): Unit = {
    logger info "Moving to DiscoveringActivity"
    startActivity[DiscoveringActivity]
  }

  def moveToSettings(): Unit = {
    logger info "Moving to SettingsActivity"
    startActivity[SettingsActivity]
  }

  def moveToLogin(): Unit = {
    logger info "Moving to LoginActivity"
    startActivity[LoginActivity]
  }

  def moveToLoginOrElse[R](moveTo: => Task[Unit]): Task[Unit] = {
    import com.talkie.client.core.facebook.FacebookServiceInterpreter._

    checkIfLoggedToFacebook.interpret.flatMap { isLogged =>
      logger trace s"User logged?: $isLogged"
      if (isLogged) moveTo
      else Task(moveToLogin())
    }.map { _ => () }
  }

  private def startActivity[A <: Activity](implicit classTag: ClassTag[A]): Unit = {
    logger trace s"Moving to ${classTag.toString}"
    activity.startActivity(new Intent(context.androidContext, classTag.runtimeClass))
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
}
