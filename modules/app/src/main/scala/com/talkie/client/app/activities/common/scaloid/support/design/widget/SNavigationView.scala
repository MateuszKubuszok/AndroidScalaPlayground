package com.talkie.client.app.activities.common.scaloid.support.design.widget

import android.content.Context
import android.support.design.widget.NavigationView
import org.scaloid.common._

class SNavigationView()(implicit context: Context, parentVGroup: TraitViewGroup[_] = null)
    extends NavigationView(context) with TraitViewGroup[SNavigationView] {

  def basis = this

  override val parentViewGroup = parentVGroup
}

object SNavigationView {
  def apply[LP <: ViewGroupLayoutParams[_, SNavigationView]]()(implicit context: Context, defaultLayoutParam: SNavigationView => LP): SNavigationView = {
    val v = new SNavigationView
    v.<<.parent.+=(v)
    v
  }

  def create[LP <: ViewGroupLayoutParams[_, SNavigationView]]()(implicit context: Context, defaultLayoutParam: SNavigationView => LP) = new SNavigationView()
}
