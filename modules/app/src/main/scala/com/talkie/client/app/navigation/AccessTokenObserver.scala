package com.talkie.client.app.navigation

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.facebook.{ AccessToken, AccessTokenTracker }
import com.talkie.client.core.events.EventBusComponent
import com.talkie.client.core.events.EventMessages.NotifyEventListenersRequest
import com.talkie.client.core.logging.LoggerComponent
import com.talkie.client.core.services.ContextComponent
import com.talkie.client.domain.events.FacebookEvents.{ TokenUpdated, LoggedOut }
import com.talkie.client.domain.services.facebook.FacebookMessages.CheckLoggedStatusRequest
import com.talkie.client.domain.services.facebook.FacebookServicesComponent

trait AccessTokenObserver extends Activity {
  self: ContextComponent with EventBusComponent with LoggerComponent =>

  private implicit val c = context
  private implicit val ec = context.executionContext

  override protected def onStart() {
    super.onStart()
    accessTokenTracker.startTracking()
    logger trace "Started tracing AccessTokens"
  }

  override protected def onRestart() {
    super.onRestart()
    accessTokenTracker.startTracking()
    logger trace "Started tracing AccessTokens"
  }

  override protected def onDestroy() {
    super.onDestroy()
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