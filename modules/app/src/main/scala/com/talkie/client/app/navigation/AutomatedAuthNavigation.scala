package com.talkie.client.app.navigation

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.facebook.{ AccessToken, AccessTokenTracker }
import com.talkie.client.core.logging.LoggerComponent
import com.talkie.client.core.services.ContextComponent
import com.talkie.client.domain.services.facebook.FacebookMessages.CheckLoggedStatusRequest
import com.talkie.client.domain.services.facebook.FacebookServicesComponent

trait AutomatedAuthNavigation extends Activity {
  self: ContextComponent with FacebookServicesComponent with LoggerComponent with ManualNavigation =>

  private implicit val c = context
  private implicit val ec = context.executionContext

  private val Extra_ActivityAfterLogin = "activity_after_login"

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

  protected def onUserLoggedIn(currentAccessToken: AccessToken) {
    returnFromLoginActivity()
    logger info "User logged in"
  }

  protected def onUserLoggedOut(oldAccessToken: AccessToken) {
    startLoginActivityThenReturnTo(mainActivity())
    logger info "User logged out"
  }

  protected def onTokenRefreshed(oldAccessToken: AccessToken, currentAccessToken: AccessToken): Unit = {
    logger debug "AccessToken refreshed"
  }

  protected def moveToMainIfLogged() =
    facebookServices.checkIfLogged(CheckLoggedStatusRequest()) map { result =>
      if (result.isLogged) {
        logger trace "Move on to MainActivity"
        startMainActivity()
      } else {
        logger trace "Move on to LoginActivity"
        startLoginActivityThenReturnTo(mainActivity())
      }
    }

  private def startLoginActivityThenReturnTo(intent: Intent) {
    logger trace s"Requested FB login then ${intent.getComponent.getClassName}"
    startActivity(loginActivity().putExtra(Extra_ActivityAfterLogin, intent))
  }

  private def returnFromLoginActivity() {
    val intent = Option(getIntent.getParcelableExtra[Intent](Extra_ActivityAfterLogin)).getOrElse(mainActivity())
    logger trace s"Logged in, now into ${intent.getComponent.getClassName}"
    startActivity(intent)
  }

  private lazy val accessTokenTracker = new AccessTokenTracker {

    override def onCurrentAccessTokenChanged(oldAccessToken: AccessToken, currentAccessToken: AccessToken) =
      (Option(oldAccessToken), Option(currentAccessToken)) match {
        case (None, Some(accessToken)) => onUserLoggedIn(currentAccessToken)
        case (Some(accessToken), None) => onUserLoggedOut(oldAccessToken)
        case _                         => onTokenRefreshed(oldAccessToken, currentAccessToken)
      }
  }
}
