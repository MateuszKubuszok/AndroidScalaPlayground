package com.talkie.client.app.activities.login

import android.view.Gravity
import com.talkie.client.app.activities.common.CommonViews
import com.talkie.client.app.activities.common.utils.{ DelayedViews, SLoginButton }
import org.scaloid.common._

trait LoginViews {

  protected def loginLayout: SRelativeLayout
  protected def loginButton: SLoginButton
}

trait LoginViewsImpl extends LoginViews with CommonViews with DelayedViews {
  self: LoginActivity =>

  private implicit val context: android.content.Context = ctx

  private val loginButtonDelayed = delay[SLoginButton]()

  override protected lazy val loginLayout: SRelativeLayout = new SRelativeLayout {
    fill

    padding(
      dimens.activityHorizontalMargin,
      dimens.activityVerticalMargin,
      dimens.activityHorizontalMargin,
      dimens.activityVerticalMargin
    )

    gravity = Gravity.CENTER

    STextView("Sign in")

    val loginButton = SLoginButton().<<
      .fw
      .marginTop(dimens.loginButtonVerticalMargin)
      .marginBottom(dimens.loginButtonVerticalMargin)
      .>>

    loginButtonDelayed success loginButton
  }

  override protected lazy val loginButton = getDelayed(loginButtonDelayed)
}
