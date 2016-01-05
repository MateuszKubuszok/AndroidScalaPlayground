package com.talkie.client.app.navigation

import android.app.Activity
import android.content.Intent
import com.talkie.client.core.events.EventListener
import com.talkie.client.core.logging.LoggerComponent
import com.talkie.client.core.services.ContextComponent
import com.talkie.client.domain.events.FacebookEvents._
import com.talkie.client.domain.services.facebook.FacebookMessages.CheckLoggedStatusRequest
import com.talkie.client.domain.services.facebook.FacebookServicesComponent

trait AuthNavigation {

  def onUserLoggedIn: EventListener[LoginSucceeded]
  def onUserLoggedOut: EventListener[LoggedOut]
  def moveToMainIfLogged(): Unit
}

trait AuthNavigationComponent {

  def authNavigation: AuthNavigation
}

trait AuthNavigationComponentImpl extends AuthNavigationComponent {
  self: Activity with ContextComponent with LoggerComponent with ManualNavigation with FacebookServicesComponent =>

  private implicit val c = context
  private implicit val ec = context.executionContext

  private val Extra_ActivityAfterLogin = "activity_after_login"

  object authNavigation extends AuthNavigation {

    object onUserLoggedIn extends EventListener[LoginSucceeded] {

      override def handleEvent(event: LoginSucceeded) = {
        logger info "User logged in"
        returnFromLoginActivity()
        true
      }
    }

    object onUserLoggedOut extends EventListener[LoggedOut] {

      override def handleEvent(event: LoggedOut) = {
        startLoginActivityThenReturnTo(discoveringActivity())
        logger info "User logged out"
        true
      }
    }

    def moveToMainIfLogged() =
      facebookServices.checkIfLogged(CheckLoggedStatusRequest()) map { result =>
        if (result.isLogged) {
          logger trace "Move on to DiscoveringActivity"
          startDiscoveringActivity()
        } else {
          logger trace "Move on to LoginActivity"
          startLoginActivityThenReturnTo(discoveringActivity())
        }
      }

    private def startLoginActivityThenReturnTo(intent: Intent) {
      logger trace s"Requested FB login then ${intent.getComponent.getClassName}"
      startActivity(loginActivity().putExtra(Extra_ActivityAfterLogin, intent))
    }

    private def returnFromLoginActivity() {
      val intent = Option(getIntent.getParcelableExtra[Intent](Extra_ActivityAfterLogin))
        .getOrElse(discoveringActivity())
      logger trace s"Logged in, now into ${intent.getComponent.getClassName}"
      startActivity(intent)
    }
  }
}
