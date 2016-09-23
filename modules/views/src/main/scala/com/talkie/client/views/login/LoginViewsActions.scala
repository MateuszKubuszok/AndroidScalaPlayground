package com.talkie.client.views.login

import com.facebook.login.widget.LoginButton
import com.talkie.client.common.components.Activity
import com.talkie.client.common.context.Context
import com.talkie.client.views.R

trait LoginViewsActions {

  def initializeLayout(): Unit

  def getLoginButton: Option[LoginButton]
}

final class LoginViewsActionsImpl(
    implicit
    context:  Context,
    activity: Activity,
    views:    LoginViews
) extends LoginViewsActions {

  def initializeLayout(): Unit = activity.setContentView(R.layout.login_activity)

  def getLoginButton: Option[LoginButton] = Option(views.loginButton)
}
