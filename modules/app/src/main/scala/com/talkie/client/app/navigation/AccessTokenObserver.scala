package com.talkie.client.app.navigation

import com.facebook.{ AccessToken, AccessTokenTracker }
import com.talkie.client.core.context.{ Context, CoreContext }
import com.talkie.client.core.events.EventBus
import com.talkie.client.core.events.EventMessages.NotifyEventListenersRequest
import com.talkie.client.domain.events.FacebookEvents.{ TokenUpdated, LoggedOut }
import com.talkie.client.domain.services.facebook.FacebookServices
import com.talkie.client.views.common.RichActivity
import org.scaloid.common.LocalService

class AccessTokenObserver(context: Context with CoreContext, eventBus: EventBus) {

  private implicit val c = context
  private implicit val ec = context.serviceExecutionContext

  private val logger = context.loggerFor(this)

  def configureBindings(activity: RichActivity) {

    activity.onStart {
      FacebookServices.ensureInitialized(context)
      accessTokenTracker.startTracking()
      logger trace "Started tracing AccessTokens"
    }

    activity.onRestart {
      FacebookServices.ensureInitialized(context)
      accessTokenTracker.startTracking()
      logger trace "Started tracing AccessTokens"
    }

    activity.onDestroy {
      accessTokenTracker.stopTracking()
      logger trace "Stopped tracing AccessTokens"
    }
  }

  def configureBindings(service: LocalService) {

    FacebookServices.ensureInitialized(context)
    accessTokenTracker.startTracking()
    logger trace "Started tracing AccessTokens"

    service.onDestroy {
      accessTokenTracker.stopTracking()
      logger trace "Stopped tracing AccessTokens"
    }
  }

  private object accessTokenTracker extends AccessTokenTracker {

    override def onCurrentAccessTokenChanged(oldAccessToken: AccessToken, currentAccessToken: AccessToken) = {
      logger trace s"Access token changed ($oldAccessToken) => ($currentAccessToken)"
      (Option(oldAccessToken), Option(currentAccessToken)) match {
        case (Some(_), Some(_)) => eventBus.notifyEventListeners(NotifyEventListenersRequest(TokenUpdated()))
        case (Some(_), None)    => // logged out, notified by FacebookServices
        case _                  => // logged in, notified by FacebookServices
      }
    }
  }
}
