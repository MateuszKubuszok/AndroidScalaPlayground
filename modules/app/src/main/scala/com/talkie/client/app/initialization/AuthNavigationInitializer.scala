package com.talkie.client.app.initialization

import com.talkie.client.app.navigation.AuthNavigationComponent
import com.talkie.client.core.events.EventBusComponent
import com.talkie.client.core.events.EventMessages.RegisterEventListenerRequest
import com.talkie.client.core.logging.LoggerComponent
import com.talkie.client.core.services.ContextComponent

private[initialization] trait AuthNavigationInitializer extends Initialization {
  self: ContextComponent with EventBusComponent with LoggerComponent with AuthNavigationComponent =>

  onInitialization {
    implicit val c = context

    eventBus.registerEventListener(RegisterEventListenerRequest(authNavigation.onUserLoggedIn))
    eventBus.registerEventListener(RegisterEventListenerRequest(authNavigation.onUserLoggedOut))

    logger info "Login initialized"
  }
}
