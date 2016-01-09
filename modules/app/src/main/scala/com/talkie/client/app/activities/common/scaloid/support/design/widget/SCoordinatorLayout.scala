package com.talkie.client.app.activities.common.scaloid.support.design.widget

import android.content.Context
import android.support.design.widget.CoordinatorLayout
import android.view.View
import org.scaloid.common._

class RichCoordinatorLayout[This <: CoordinatorLayout](val basis: This) extends TraitCoordinatorLayout[This]

trait TraitCoordinatorLayout[This <: CoordinatorLayout] extends TraitViewGroup[This] {

  // TODO: add missing features
}

class SCoordinatorLayout()(implicit context: Context, parentVGroup: TraitViewGroup[_] = null)
    extends CoordinatorLayout(context) with TraitCoordinatorLayout[SCoordinatorLayout] {

  def basis = this
  override val parentViewGroup = parentVGroup
  implicit def defaultLayoutParams[V <: View](v: V): LayoutParams[V] = v.getLayoutParams() match {
    case p: LayoutParams[V @unchecked] => p
    case _                             => new LayoutParams(v)
  }

  class LayoutParams[V <: View](v: V) extends CoordinatorLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT) with ViewGroupMarginLayoutParams[LayoutParams[V], V] {
    def basis = this

    v.setLayoutParams(this)

    def Gravity(g: Int) = {
      gravity = g
      this
    }

    def parent = SCoordinatorLayout.this

    def >> : V = v

  }
}

object SCoordinatorLayout {
  def apply[LP <: ViewGroupLayoutParams[_, SCoordinatorLayout]]()(implicit context: Context, defaultLayoutParam: SCoordinatorLayout => LP): SCoordinatorLayout = {
    val v = new SCoordinatorLayout
    v.<<.parent.+=(v)
    v
  }

}
