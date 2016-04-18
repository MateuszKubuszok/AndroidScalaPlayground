package com.talkie.client.views.login

import android.widget.RelativeLayout
import com.facebook.login.widget.LoginButton
import com.talkie.client.views.common.views.TypedFindLayout
import com.talkie.client.views.{ TR, TypedFindView }

trait LoginViews {

  protected def loginButton: LoginButton
  protected def layout: RelativeLayout
}

trait LoginViewsImpl extends LoginViews {
  self: TypedFindView with TypedFindLayout =>

  override protected lazy val layout = findLayout(TR.layout.activity_login)
  override protected lazy val loginButton = findView(TR.login_button)
}
