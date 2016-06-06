package com.talkie.client.views.login

import com.facebook.login.widget.LoginButton
import com.talkie.client.common.components.Activity
import com.talkie.client.common.context.Context
import com.talkie.client.views.R

private[login] final class LoginViewsActions(
    context:  Context,
    activity: Activity,
    views:    LoginViews
) {

  def initializeLayout(): Unit = activity.setContentView(R.layout.activity_login)

  def getLoginButton: Option[LoginButton] = Option(views.loginButton)
}
