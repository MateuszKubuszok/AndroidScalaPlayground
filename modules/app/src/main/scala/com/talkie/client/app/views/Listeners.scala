package com.talkie.client.app.views

import android.location.{ Location, LocationListener }
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener

trait Listeners {

  final protected def makeLocationListener(
    onLocationChangedF:  Location => Unit              = (_) => (),
    onStatusChangedF:    (String, Int, Bundle) => Unit = (_, _, _) => (),
    onProviderEnabledF:  String => Unit                = (_) => (),
    onProviderDisabledF: String => Unit                = (_) => ()
  ) = new LocationListener {

    override def onLocationChanged(location: Location): Unit = onLocationChangedF(location)

    override def onStatusChanged(provider: String, status: Int, extras: Bundle) = onStatusChangedF(provider, status, extras)

    override def onProviderEnabled(provider: String) = onProviderEnabledF(provider)

    override def onProviderDisabled(provider: String) = onProviderDisabledF(provider)
  }

  final protected def makeOnClickListener(action: View => Unit) = new OnClickListener {

    override def onClick(view: View) = action(view)
  }

  final protected def setOnClickListener(action: View => Unit) = { view: View =>
    view setOnClickListener makeOnClickListener(action)
  }
}
