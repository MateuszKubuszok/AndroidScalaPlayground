package com.talkie.client.app.navigation

import com.facebook.{ AccessToken, AccessTokenTracker }
import com.talkie.client.core.events.EventBus
import com.talkie.client.core.events.EventMessages.NotifyEventListenersRequest
import com.talkie.client.core.services.Context
import com.talkie.client.domain.events.FacebookEvents.{ TokenUpdated, LoggedOut }
import com.talkie.client.views.common.RichActivity

class AccessTokenObserver(activity: RichActivity, context: Context, eventBus: EventBus) {

  private implicit val c = context
  private implicit val ec = context.serviceExecutionContext

  private val logger = context.loggerFor(this)

  activity.onStart {
    accessTokenTracker.startTracking()
    logger trace "Started tracing AccessTokens"
  }

  activity.onRestart {
    accessTokenTracker.startTracking()
    logger trace "Started tracing AccessTokens"
  }

  activity.onDestroy {
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
