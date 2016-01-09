package com.talkie.client.app.activities.login

import android.view.Gravity
import com.talkie.client.app.activities.common.RichActivity
import com.talkie.client.app.activities.common.scaloid.facebook.SLoginButton
import com.talkie.client.app.activities.common.views.{ CommonViews, DelayedViews }
import org.scaloid.common._

trait LoginViews {

  protected def loginButton: SLoginButton
  protected def layout: SRelativeLayout
}

trait LoginViewsImpl extends LoginViews with CommonViews with DelayedViews {
  self: RichActivity =>

  private implicit val context: android.content.Context = ctx

  private val loginButtonDelayed = delay[SLoginButton]()
  private val layoutDelayed = delay[SRelativeLayout]()

  override protected lazy val layout = getDelayed(layoutDelayed)
  override protected lazy val loginButton = getDelayed(loginButtonDelayed)

  initiateWith(layoutDelayed) {
    new SRelativeLayout {
      fill
      padding(
        dimension.activityHorizontalMargin,
        dimension.activityVerticalMargin,
        dimension.activityHorizontalMargin,
        dimension.activityVerticalMargin
      )
      gravity = Gravity.CENTER

      new STextView("Sign in") {
        fw
      } appendTo this

      loginButtonDelayed resolve new SLoginButton {
        fw
      }.<<
        .marginTop(dimension.loginButtonVerticalMargin)
        .marginBottom(dimension.loginButtonVerticalMargin)
        .>> appendTo this
    }
  }
}
