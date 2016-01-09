package com.talkie.client.app.activities.common.scaloid.support.v4.widget

import android.support.v4.widget.DrawerLayout
import android.view.View
import org.scaloid.common._

class RichDrawerLayout[This <: DrawerLayout](val basis: This) extends TraitDrawerLayout[This]

trait TraitDrawerLayout[This <: DrawerLayout] extends TraitViewGroup[This] {

  // TODO: add missing features
}

class SDrawerLayout()(implicit context: android.content.Context, parentVGroup: TraitViewGroup[_] = null)
    extends DrawerLayout(context) with TraitDrawerLayout[SDrawerLayout] {

  def basis = this
  override val parentViewGroup = parentVGroup
  implicit def defaultLayoutParams[V <: View](v: V): LayoutParams[V] = v.getLayoutParams() match {
    case p: LayoutParams[V @unchecked] => p
    case _                             => new LayoutParams(v)
  }

  class LayoutParams[V <: View](v: V) extends DrawerLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT) with ViewGroupMarginLayoutParams[LayoutParams[V], V] {
    def basis = this

    v.setLayoutParams(this)

    def Gravity(g: Int) = {
      gravity = g
      this
    }

    def parent = SDrawerLayout.this

    def >> : V = v

  }
}

object SDrawerLayout {
  def apply[LP <: ViewGroupLayoutParams[_, SDrawerLayout]]()(implicit context: android.content.Context, defaultLayoutParam: SDrawerLayout => LP): SDrawerLayout = {
    val v = new SDrawerLayout
    v.<<.parent.+=(v)
    v
  }

}
