package com.talkie.client.views.login

import android.widget.RelativeLayout
import com.facebook.login.widget.LoginButton
import com.talkie.client.common.components.Activity
import com.talkie.client.views.common.views.TypedFindLayout
import com.talkie.client.views.{ TR, TypedFindView }

private[login] trait LoginViews {

  def loginButton: LoginButton
  def layout: RelativeLayout
}

private[login] final class LoginViewsImpl(
    activity: Activity
) extends LoginViews with TypedFindView with TypedFindLayout {

  override protected def findViewById(id: Int) = activity.findViewById(id)
  override lazy val layout = findLayout(TR.layout.activity_login)
  override lazy val loginButton = findView(TR.login_button)
}
