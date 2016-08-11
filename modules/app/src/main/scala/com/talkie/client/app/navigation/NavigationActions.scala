package com.talkie.client.app.navigation

import android.content.Intent
import com.talkie.client.app.activities.discovering.DiscoveringActivity
import com.talkie.client.app.activities.login.LoginActivity
import com.talkie.client.app.activities.settings.SettingsActivity
import com.talkie.client.common.components.Activity
import com.talkie.client.common.context.Context
import com.talkie.client.common.events.EventListener
import com.talkie.client.core.events.EventActions
import com.talkie.client.core.facebook._

import scala.reflect.ClassTag

trait NavigationActions {

  def configureNavigation(): Unit

  def moveToDiscovering(): Unit

  def moveToSettings(): Unit

  def moveToLogin(): Unit

  def moveToLoginOrElse[R](moveTo: => Unit): Unit
}

final class NavigationActionsImpl(
    implicit
    context:         Context,
    activity:        Activity,
    eventActions:    EventActions,
    facebookActions: FacebookActions
) extends NavigationActions {

  private val logger = context loggerFor this

  def configureNavigation(): Unit = {

    activity.bootstrap {
      eventActions.registerEventListener(OnUserLoggedIn)
      eventActions.registerEventListener(OnUserLoggedOut)
    }

    activity.teardown {
      eventActions.removeEventListener(OnUserLoggedIn)
      eventActions.removeEventListener(OnUserLoggedOut)
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

  def moveToLoginOrElse[R](moveTo: => Unit): Unit = {
    val isLogged = facebookActions.checkIfLogged()
    logger trace s"User logged?: $isLogged"
    if (isLogged) moveTo
    else moveToLogin()
  }

  private def startActivity[A <: Activity](implicit classTag: ClassTag[A]): Unit = {
    logger trace s"Moving to ${classTag.toString}"
    activity.startActivity(new Intent(context.androidContext, classTag.runtimeClass))
  }

  private object OnUserLoggedIn extends EventListener[LoginSucceeded] {

    override def handleEvent(event: LoginSucceeded) = {
      moveToDiscovering()
      true
    }
  }

  private object OnUserLoggedOut extends EventListener[LoggedOut] {

    override def handleEvent(event: LoggedOut) = {
      moveToLogin()
      true
    }
  }
}
