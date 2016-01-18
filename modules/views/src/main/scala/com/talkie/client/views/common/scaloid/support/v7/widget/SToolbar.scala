package com.talkie.client.views.common.scaloid.support.v7.widget

import android.content.Context
import android.support.v7.widget.Toolbar
import org.scaloid.common._

class SToolbar()(implicit context: Context, parentVGroup: TraitViewGroup[_] = null)
    extends Toolbar(context) with TraitViewGroup[SToolbar] {

  def basis = this

  override val parentViewGroup = parentVGroup
}

object SToolbar {
  def apply[LP <: ViewGroupLayoutParams[_, SToolbar]]()(implicit context: Context, defaultLayoutParam: SToolbar => LP): SToolbar = {
    val v = new SToolbar
    v.<<.parent.+=(v)
    v
  }

  def create[LP <: ViewGroupLayoutParams[_, SToolbar]]()(implicit context: Context, defaultLayoutParam: SToolbar => LP) = new SToolbar()
}
