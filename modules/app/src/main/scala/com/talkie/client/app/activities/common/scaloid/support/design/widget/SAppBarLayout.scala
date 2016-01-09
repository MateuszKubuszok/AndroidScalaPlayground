package com.talkie.client.app.activities.common.scaloid.support.design.widget

import android.content.Context
import android.support.design.widget.AppBarLayout
import android.view.View
import org.scaloid.common._

class RichAppBarLayout[This <: AppBarLayout](val basis: This) extends TraitAppBarLayout[This]

trait TraitAppBarLayout[This <: AppBarLayout] extends TraitViewGroup[This] {

  // TODO: add missing features
}

class SAppBarLayout()(implicit context: Context, parentVGroup: TraitViewGroup[_] = null)
    extends AppBarLayout(context) with TraitAppBarLayout[SAppBarLayout] {

  def basis = this
  override val parentViewGroup = parentVGroup
  implicit def defaultLayoutParams[V <: View](v: V): LayoutParams[V] = v.getLayoutParams() match {
    case p: LayoutParams[V @unchecked] => p
    case _                             => new LayoutParams(v)
  }

  class LayoutParams[V <: View](v: V) extends AppBarLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT) with ViewGroupMarginLayoutParams[LayoutParams[V], V] {
    def basis = this

    v.setLayoutParams(this)

    def Gravity(g: Int) = {
      gravity = g
      this
    }

    def parent = SAppBarLayout.this

    def >> : V = v

  }
}

object SAppBarLayout {
  def apply[LP <: ViewGroupLayoutParams[_, SAppBarLayout]]()(implicit context: Context, defaultLayoutParam: SAppBarLayout => LP): SAppBarLayout = {
    val v = new SAppBarLayout
    v.<<.parent.+=(v)
    v
  }

}
