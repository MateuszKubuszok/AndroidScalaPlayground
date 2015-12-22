package com.talkie.client.facebook

import android.content.Intent
import android.os.Bundle
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.facebook.{CallbackManager, FacebookCallback, FacebookException}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Promise

trait FacebookLoggingIn extends FacebookActivity {

  private val Permissions = List("user_friends") // TODO: change into sth more reasonable

  private val callbackManagerP = Promise[CallbackManager]()
  private val callbackManagerF = callbackManagerP.future

  override protected def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    callbackManagerP.success(CallbackManager.Factory.create())
  }

  override protected def onPostCreate(savedInstanceState: Bundle) {
    super.onPostCreate(savedInstanceState)
    loginButtonOpt match {
      case Some(loginButton) => setLoginButton(loginButton)
      case None => logger assert "Button should have been available!"
    }
  }

  override protected def onActivityResult(requestCode: Int, resultCode: Int, data: Intent) =
    callbackManagerF foreach { _.onActivityResult(requestCode, resultCode, data) }

  private def setLoginButton(loginButton: LoginButton) {
    loginButton.setReadPermissions(Permissions: _*)
    callbackManagerF foreach (loginButton.registerCallback(_, new LoginCallback))
  }

  private class LoginCallback extends FacebookCallback[LoginResult] {

    override def onSuccess(result: LoginResult) {
      logger trace s"FB login result: $result"
      returnFromLoginActivity()
    }

    override def onCancel() {
      logger warn "User canceled login"
    }

    override def onError(error: FacebookException) {
      logger warn "Login error"
    }
  }
}
