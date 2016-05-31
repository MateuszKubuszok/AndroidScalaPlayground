package com.talkie.client.app.activities.login

import com.talkie.client.app.activities.common.Controller
import com.talkie.client.core.components.Activity
import com.talkie.client.views.login.LoginViews
import com.talkie.client.views.R

trait LoginController extends Controller {
  self: Activity with LoginViews =>

  override protected def loginButtonOpt() = Option(loginButton)

  onCreate {
    setContentView(R.layout.activity_login)
  }
}
