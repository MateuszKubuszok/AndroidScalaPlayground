package com.talkie.client.core.facebook

import android.content.Intent
import com.facebook._
import com.facebook.login.widget.LoginButton
import com.facebook.login.{ LoginResult, LoginManager }
import com.talkie.client.common.components.Activity
import com.talkie.client.common.context.Context
import com.talkie.client.core.events.EventActions

trait FacebookActions {

  def checkIfLogged(): Boolean

  def configureLogin(requstor: Activity, loginButtonOpt: () => Option[LoginButton]): Unit

  def logout(): Unit
}

final class FacebookActionsImpl(
    implicit
    context:      Context,
    eventActions: EventActions
) extends FacebookActions {

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
        case (Some(_), Some(_)) => eventActions.notifyEventListeners(TokenUpdated())
        case (Some(_), None)    => // logged out, notified by LoginResultCallback
        case _                  => // logged in, notified by LoginResultCallback
      }
    }
  }

  private object LoginResultCallback extends FacebookCallback[LoginResult] {

    override def onSuccess(result: LoginResult) = {
      logger info s"Logged in"
      eventActions.notifyEventListeners(LoginSucceeded(result))
    }

    override def onCancel() = {
      logger info "Login cancelled"
      eventActions.notifyEventListeners(LoginCancelled())
    }

    override def onError(error: FacebookException) = {
      logger error ("Login failed", error)
      eventActions.notifyEventListeners(LoginFailed(error))
    }
  }

  def checkIfLogged(): Boolean = {
    ensureInitialized()
    logger trace "Requested login status check"
    Option(Profile.getCurrentProfile).isDefined
  }

  def configureLogin(requstor: Activity, loginButtonOpt: () => Option[LoginButton]): Unit = {
    ensureInitialized()
    logger trace "Requested activity and loginButton configuration"
    val tracker = new TokenUpdateTracker

    requstor.bootstrap {
      tracker.startTracking()
      loginButtonOpt().foreach { loginButton =>
        loginButton.setReadPermissions(permissions: _*)
        loginButton.registerCallback(callBackManager, LoginResultCallback)
        logger debug "Configured login button"
      }
      logger trace "Started tracing AccessTokens"
    }

    requstor.teardown {
      tracker.stopTracking()
      logger trace "Stopped tracing AccessTokens"
    }

    requstor.onActivityResult { (requestCode: Int, resultCode: Int, data: Intent) =>
      callBackManager.onActivityResult(requestCode, resultCode, data)
    }
  }

  def logout(): Unit = {
    ensureInitialized()
    logger debug "Requested logout"
    loginManager.logOut()
    eventActions.notifyEventListeners(LoggedOut())
  }
}
