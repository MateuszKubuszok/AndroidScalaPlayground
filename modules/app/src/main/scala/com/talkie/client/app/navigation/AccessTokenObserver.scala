package com.talkie.client.app.navigation

import com.facebook.{ AccessToken, AccessTokenTracker }
import com.talkie.client.core.events.EventBusComponent
import com.talkie.client.core.events.EventMessages.NotifyEventListenersRequest
import com.talkie.client.core.logging.LoggerComponent
import com.talkie.client.core.services.ContextComponent
import com.talkie.client.domain.events.FacebookEvents.{ TokenUpdated, LoggedOut }
import com.talkie.client.views.common.RichActivity

trait AccessTokenObserver {
  self: RichActivity with ContextComponent with EventBusComponent with LoggerComponent =>

  private implicit val c = context
  private implicit val ec = context.executionContext

  onStart {
    accessTokenTracker.startTracking()
    logger trace "Started tracing AccessTokens"
  }

  onRestart {
    accessTokenTracker.startTracking()
    logger trace "Started tracing AccessTokens"
  }

  onDestroy {
    accessTokenTracker.stopTracking()
    logger trace "Stopped tracing AccessTokens"
  }

  private object accessTokenTracker extends AccessTokenTracker {

    override def onCurrentAccessTokenChanged(oldAccessToken: AccessToken, currentAccessToken: AccessToken) = {
      logger trace s"Access token changed ($oldAccessToken) => ($currentAccessToken)"
      (Option(oldAccessToken), Option(currentAccessToken)) match {
        case (Some(_), Some(_)) => eventBus.notifyEventListeners(NotifyEventListenersRequest(TokenUpdated()))
        case (Some(_), None)    => eventBus.notifyEventListeners(NotifyEventListenersRequest(LoggedOut()))
        case _                  => // logged in, notified by FacebookServicesComponentImpl
      }
    }
  }
}
