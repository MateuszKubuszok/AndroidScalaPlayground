package com.talkie.client.views.main

import android.widget.LinearLayout
import com.talkie.client.views.common.views.TypedFindLayout
import com.talkie.client.views.{ TR, TypedFindView }

trait MainViews {

  protected def layout: LinearLayout
}

trait MainViewsImpl extends MainViews {
  self: TypedFindView with TypedFindLayout =>

  override protected lazy val layout = findLayout(TR.layout.activity_main)
}
