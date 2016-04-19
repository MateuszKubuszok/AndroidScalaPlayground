package com.talkie.client.app.initialization

import com.talkie.client.app.navigation.AuthNavigation
import com.talkie.client.core.events.EventBus
import com.talkie.client.core.events.EventMessages.RegisterEventListenerRequest
import com.talkie.client.core.services.Context

class AuthNavigationInitializer(
    context:        Context,
    eventBus:       EventBus,
    authNavigation: AuthNavigation
) extends Initialization {

  def initialize() {
    implicit val c = context

    eventBus.registerEventListener(RegisterEventListenerRequest(authNavigation.onUserLoggedIn))
    eventBus.registerEventListener(RegisterEventListenerRequest(authNavigation.onUserLoggedOut))

    context.loggerFor(this) info "Login initialized"
  }
}
