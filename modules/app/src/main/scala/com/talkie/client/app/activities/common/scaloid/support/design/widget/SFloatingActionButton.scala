package com.talkie.client.app.activities.common.scaloid.support.design.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.design.widget.FloatingActionButton
import org.scaloid.common._

class SFloatingActionButton()(implicit context: Context, parentVGroup: TraitViewGroup[_] = null)
    extends FloatingActionButton(context) with TraitImageButton[SFloatingActionButton] {

  def basis = this
  override val parentViewGroup = parentVGroup

  def this(imageResource: Drawable)(implicit context: Context) = {
    this()
    this.imageDrawable = imageResource
  }

  def this(imageResource: Drawable, ignore: Nothing)(implicit context: Context) = this() // Just for implicit conversion of ViewOnClickListener

  def this(imageResource: Drawable, onClickListener: ViewOnClickListener, interval: Int)(implicit context: Context) = {
    this()
    this.imageDrawable = imageResource
    this.setOnClickListener(onClickListener.onClickListener)
    if (interval >= 0) onPressAndHold(interval, onClickListener.func(this))
  }

  def this(imageResource: Drawable, onClickListener: ViewOnClickListener)(implicit context: Context) = this(imageResource, onClickListener, -1)

}

object SFloatingActionButton extends ImageViewCompanion[SFloatingActionButton] {
  def apply[LP <: ViewGroupLayoutParams[_, SFloatingActionButton]]()(implicit context: Context, defaultLayoutParam: SFloatingActionButton => LP): SFloatingActionButton = {
    val v = new SFloatingActionButton
    v.<<.parent.+=(v)
    v
  }

  def create[LP <: ViewGroupLayoutParams[_, SFloatingActionButton]]()(implicit context: Context, defaultLayoutParam: SFloatingActionButton => LP) = new SFloatingActionButton()
}
