package com.talkie.client.views

import android.view.View
import android.view.View.OnClickListener

trait Listeners {

  final protected def makeOnClickListener(action: View => Unit) = new OnClickListener {

    override def onClick(view: View) = action(view)
  }

  final protected def setOnClickListener(action: View => Unit) = { view: View =>
    view setOnClickListener makeOnClickListener(action)
  }
}
