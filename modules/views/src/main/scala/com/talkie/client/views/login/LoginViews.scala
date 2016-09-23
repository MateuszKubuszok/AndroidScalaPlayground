package com.talkie.client.views.login

import android.widget.RelativeLayout
import com.facebook.login.widget.LoginButton
import com.talkie.client.common.components.Activity
import com.talkie.client.views.common.views.TypedFindLayout
import com.talkie.client.views.{ TR, TypedFindView }

trait LoginViews {

  def loginButton: LoginButton
  def layout: RelativeLayout
}

final class LoginViewsImpl(
    implicit
    activity: Activity
) extends LoginViews with TypedFindView with TypedFindLayout {

  override protected def findViewById(id: Int) = activity.findViewById(id)
  override def layout = findLayout(TR.layout.login_activity)
  override lazy val loginButton = findView(TR.login_button)
}
