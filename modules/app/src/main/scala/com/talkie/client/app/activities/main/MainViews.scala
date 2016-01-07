package com.talkie.client.app.activities.main

import android.view.Gravity
import com.talkie.client.app.activities.common.CommonViews
import org.scaloid.common._

trait MainViews {

  protected def mainLayout: SVerticalLayout
}

trait MainViewsImpl extends MainViews with CommonViews {
  self: MainActivity =>

  private implicit val context: android.content.Context = ctx

  override protected lazy val mainLayout = new SVerticalLayout {
    fill

    gravity = Gravity.CENTER

    STextView("Please wait, loading...").<<
      .fill
      .Gravity(Gravity.CENTER)
      .>>
  }
}
