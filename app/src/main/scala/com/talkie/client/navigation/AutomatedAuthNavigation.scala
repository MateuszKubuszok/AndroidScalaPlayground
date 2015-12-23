package com.talkie.client.navigation

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.facebook.{AccessToken, AccessTokenTracker}
import com.talkie.client.services.{ContextComponent, LoggerComponent}
import com.talkie.client.services.facebook.FacebookMessages.CheckLoggedStatusRequest
import com.talkie.client.services.facebook.FacebookServicesComponent

trait AutomatedAuthNavigation extends Activity {
  self: ContextComponent
    with FacebookServicesComponent
    with LoggerComponent
    with ManualNavigation =>

  private implicit val c = context

  private val Extra_ActivityAfterLogin = "activity_after_login"

  override protected def onStart() {
    super.onStart()
    accessTokenTracker.startTracking()
  }

  override protected def onRestart() {
    super.onRestart()
    accessTokenTracker.startTracking()
  }

  override protected def onDestroy() {
    super.onDestroy()
    accessTokenTracker.stopTracking()
  }

  protected def onUserLoggedIn(currentAccessToken: AccessToken) {
    returnFromLoginActivity()
  }

  protected def onUserLoggedOut(oldAccessToken: AccessToken) {
    startLoginActivityThenReturnTo(mainActivity())
  }

  protected def onTokenRefreshed(oldAccessToken: AccessToken, currentAccessToken: AccessToken) {}

  protected def moveToMainIfLogged() =
    if (facebookServices.checkIfLogged(CheckLoggedStatusRequest()).isLogged) {
      logger trace "Move on to MainActivity"
      startMainActivity()
    } else {
      logger trace "Move on to LoginActivity"
      startLoginActivityThenReturnTo(mainActivity())
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
        case _ => onTokenRefreshed(oldAccessToken, currentAccessToken)
      }
  }
}
