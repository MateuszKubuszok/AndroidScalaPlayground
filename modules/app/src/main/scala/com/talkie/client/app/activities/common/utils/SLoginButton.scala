package com.talkie.client.app.activities.common.utils

import android.content.Context
import com.facebook.login.widget.LoginButton
import org.scaloid.common._

class SLoginButton()(implicit context: android.content.Context, parentVGroup: TraitViewGroup[_] = null)
    extends LoginButton(context) with TraitButton[SLoginButton] {

  def basis = this
  override val parentViewGroup = parentVGroup

  def this(text: CharSequence)(implicit context: Context) = {
    this()
    this.text = text
  }

  def this(text: CharSequence, ignore: Nothing)(implicit context: Context) =
    this() // Just for implicit conversion of ViewOnClickListener

  def this(text: CharSequence, onClickListener: ViewOnClickListener, interval: Int)(implicit context: Context) = {
    this()
    this.text = text
    this.setOnClickListener(onClickListener.onClickListener)
    if (interval >= 0) onPressAndHold(interval, onClickListener.func(this))
  }

  def this(text: CharSequence, onClickListener: ViewOnClickListener)(implicit context: Context) =
    this(text, onClickListener, -1)
}

object SLoginButton extends TextViewCompanion[SLoginButton] {
  def apply[LP <: ViewGroupLayoutParams[_, SLoginButton]]()(implicit context: android.content.Context, defaultLayoutParam: SLoginButton => LP): SLoginButton = {
    val v = new SLoginButton
    v.<<.parent.+=(v)
    v
  }

  def create[LP <: ViewGroupLayoutParams[_, SLoginButton]]()(implicit context: Context, defaultLayoutParam: SLoginButton => LP) = new SLoginButton()
}
