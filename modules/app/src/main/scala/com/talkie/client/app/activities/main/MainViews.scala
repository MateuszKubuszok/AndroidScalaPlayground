package com.talkie.client.app.activities.main

import android.view.Gravity
import com.talkie.client.app.activities.common.RichActivity
import com.talkie.client.app.activities.common.views.{ DelayedViews, CommonViews }
import org.scaloid.common._

trait MainViews {

  protected def layout: SVerticalLayout
}

trait MainViewsImpl extends MainViews with CommonViews with DelayedViews {
  self: RichActivity =>

  private implicit val context: android.content.Context = ctx

  private val layoutDelayed = delay[SVerticalLayout]()

  override protected lazy val layout = getDelayed(layoutDelayed)

  initiateWith(layoutDelayed) {
    new SVerticalLayout {
      fill
      gravity = Gravity.CENTER

      new STextView("Please wait, loading...") {
        fill
        gravity = Gravity.CENTER
      } appendTo this
    }
  }
}
