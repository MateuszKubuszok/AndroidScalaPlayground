package com.talkie.client.app.navigation

import android.app.Activity
import com.talkie.client.core.context.Context
import com.talkie.client.core.events.EventListener
import com.talkie.client.domain.events.FacebookEvents._
import com.talkie.client.domain.services.facebook.FacebookMessages.CheckLoggedStatusRequest
import com.talkie.client.domain.services.facebook.FacebookServices

trait AuthNavigation {

  def onUserLoggedIn: EventListener[LoginSucceeded]
  def onUserLoggedOut: EventListener[LoggedOut]
  def moveDependingOnLoginState(): Unit
}

class AuthNavigationImpl(
    activity:         Activity,
    context:          Context,
    manualNavigation: ManualNavigation,
    facebookServices: FacebookServices
) extends AuthNavigation {

  private implicit val c = context
  private implicit val ec = context.serviceExecutionContext

  private val logger = context.loggerFor(this)

  object onUserLoggedIn extends EventListener[LoginSucceeded] {

    override def handleEvent(event: LoginSucceeded) = {
      logger info "User logged in"
      manualNavigation.startDiscoveringActivity()
      true
    }
  }

  object onUserLoggedOut extends EventListener[LoggedOut] {

    override def handleEvent(event: LoggedOut) = {
      logger info "User logged out"
      manualNavigation.startLoginActivity()
      true
    }
  }

  def moveDependingOnLoginState() =
    facebookServices.checkIfLogged(CheckLoggedStatusRequest()) map { result =>
      if (result.isLogged) {
        logger trace "Move on to DiscoveringActivity"
        manualNavigation.startDiscoveringActivity()
      } else {
        logger trace "Move on to LoginActivity"
        manualNavigation.startLoginActivity()
      }
    }
}
