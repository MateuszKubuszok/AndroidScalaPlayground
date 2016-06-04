package com.talkie.client.core.facebook

import android.content.Intent
import com.facebook._
import com.facebook.login.widget.LoginButton
import com.facebook.login.{ LoginResult, LoginManager }
import com.talkie.client.common.components.Activity
import com.talkie.client.common.context.Context
import com.talkie.client.core.events.EventService._
import com.talkie.client.core.events.EventServiceInterpreter
import com.talkie.client.core.events.EventServiceInterpreter._

private[facebook] final class FacebookActions(
    context:              Context,
    activity:             Activity,
    implicit val eventSI: EventServiceInterpreter
) {

  private val logger = context loggerFor this

  private lazy val callBackManager = CallbackManager.Factory.create() // TODO: inject
  private lazy val loginManager = LoginManager.getInstance() // TODO: inject
  private val permissions = List("public_profile", "user_photos")

  private def ensureInitialized(): Unit = {
    if (!FacebookSdk.isInitialized) {
      require(context.androidContext.getApplicationContext != null, "ApplicationContext must be available")
      FacebookSdk.sdkInitialize(context.androidContext.getApplicationContext)
      logger info "Facebook SDK initialized"
    }
  }

  private class TokenUpdateTracker extends AccessTokenTracker {

    override def onCurrentAccessTokenChanged(oldAccessToken: AccessToken, currentAccessToken: AccessToken) = {
      logger trace s"Access token changed ($oldAccessToken) => ($currentAccessToken)"
      (Option(oldAccessToken), Option(currentAccessToken)) match {
        case (Some(_), Some(_)) => notifyEventListeners(TokenUpdated()).fireAndForget()
        case (Some(_), None)    => // logged out, notified by LoginResultCallback
        case _                  => // logged in, notified by LoginResultCallback
      }
    }
  }

  private object LoginResultCallback extends FacebookCallback[LoginResult] {

    override def onSuccess(result: LoginResult) = {
      logger info s"Logged in"
      notifyEventListeners(LoginSucceeded(result)).fireAndForget()
    }

    override def onCancel() = {
      logger info "Login cancelled"
      notifyEventListeners(LoginCancelled()).fireAndForget()
    }

    override def onError(error: FacebookException) = {
      logger error ("Login failed", error)
      notifyEventListeners(LoginFailed(error)).fireAndForget()
    }
  }

  def checkIfLogged(): Boolean = {
    ensureInitialized()
    logger trace "Requested login status check"
    Option(Profile.getCurrentProfile).isDefined
  }

  def configureLogin(loginButtonOpt: () => Option[LoginButton]): Unit = {
    ensureInitialized()
    logger trace "Requested activity and loginButton configuration"
    val tracker = new TokenUpdateTracker

    activity.bootstrap {
      tracker.startTracking()
      loginButtonOpt().foreach { loginButton =>
        loginButton.setReadPermissions(permissions: _*)
        loginButton.registerCallback(callBackManager, LoginResultCallback)
        logger debug "Configured login button"
      }
      logger trace "Started tracing AccessTokens"
    }

    activity.teardown {
      tracker.stopTracking()
      logger trace "Stopped tracing AccessTokens"
    }

    activity.onActivityResult { (requestCode: Int, resultCode: Int, data: Intent) =>
      callBackManager.onActivityResult(requestCode, resultCode, data)
    }
  }

  def logout(): Unit = {
    ensureInitialized()
    logger debug "Requested logout"
    loginManager.logOut()
    notifyEventListeners(LoggedOut()).fireAndForget()
  }
}
