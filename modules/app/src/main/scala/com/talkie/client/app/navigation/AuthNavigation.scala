package com.talkie.client.app.navigation

import android.content.Intent
import com.talkie.client.core.events.EventListener
import com.talkie.client.core.services.Context
import com.talkie.client.domain.events.FacebookEvents._
import com.talkie.client.domain.services.facebook.FacebookMessages.CheckLoggedStatusRequest
import com.talkie.client.domain.services.facebook.FacebookServices
import com.talkie.client.views.common.RichActivity

trait AuthNavigation {

  def onUserLoggedIn: EventListener[LoginSucceeded]
  def onUserLoggedOut: EventListener[LoggedOut]
  def moveToMainIfLogged(): Unit
}

class AuthNavigationImpl(
    activity:            RichActivity,
    context:             Context,
    manualNavigation:    ManualNavigation,
    facebookServices:    FacebookServices,
    accessTokenObserver: AccessTokenObserver
) extends AuthNavigation {

  private implicit val c = context
  private implicit val ec = context.serviceExecutionContext

  private val logger = context.loggerFor(this)

  private val Extra_ActivityAfterLogin = "activity_after_login"

  object onUserLoggedIn extends EventListener[LoginSucceeded] {

    override def handleEvent(event: LoginSucceeded) = {
      logger info "User logged in"
      returnFromLoginActivity()
      true
    }
  }

  object onUserLoggedOut extends EventListener[LoggedOut] {

    override def handleEvent(event: LoggedOut) = {
      startLoginActivityThenReturnTo(manualNavigation.discoveringActivity())
      logger info "User logged out"
      true
    }
  }

  def moveToMainIfLogged() =
    facebookServices.checkIfLogged(CheckLoggedStatusRequest()) map { result =>
      if (result.isLogged) {
        logger trace "Move on to DiscoveringActivity"
        manualNavigation.startDiscoveringActivity()
      } else {
        logger trace "Move on to LoginActivity"
        startLoginActivityThenReturnTo(manualNavigation.discoveringActivity())
      }
    }

  private def startLoginActivityThenReturnTo(intent: Intent) {
    logger trace s"Requested FB login then ${intent.getComponent.getClassName}"
    activity.startActivity(manualNavigation.loginActivity().putExtra(Extra_ActivityAfterLogin, intent))
  }

  private def returnFromLoginActivity() {
    val intent = Option(activity.getIntent.getParcelableExtra[Intent](Extra_ActivityAfterLogin))
      .getOrElse(manualNavigation.discoveringActivity())
    logger trace s"Logged in, now into ${intent.getComponent.getClassName}"
    activity.startActivity(intent)
  }
}
