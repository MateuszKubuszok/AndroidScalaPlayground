package com.talkie.client.views.main

import android.view.Gravity
import com.talkie.client.views.common.RichActivity
import com.talkie.client.views.common.views.{ CommonViews, DelayedViews }
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
