package com.talkie.client.common

import android.view.View
import android.view.View.OnClickListener

trait ListenerUtils {

  protected def makeOnClickListener(action: View => Unit) = new OnClickListener {
    override def onClick(view: View): Unit = action(view)
  }

  protected def setOnClickListener(action: View => Unit) = { view: View =>
    view setOnClickListener makeOnClickListener(action)
  }
}
