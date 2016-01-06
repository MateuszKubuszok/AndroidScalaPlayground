package com.talkie.client.app.activities.common

import android.location.Location
import android.os.Bundle
import android.preference.Preference
import android.view.View

object Listeners {

  case class LocationListener(
      onLocationChangedFn:  Location => Unit              = _ => {},
      onStatusChangedFn:    (String, Int, Bundle) => Unit = (_, _, _) => {},
      onProviderEnabledFn:  String => Unit                = _ => {},
      onProviderDisabledFn: String => Unit                = _ => {}
  ) extends android.location.LocationListener {

    override def onLocationChanged(location: Location) = onLocationChangedFn(location)
    override def onStatusChanged(provider: String, status: Int, extras: Bundle) =
      onStatusChangedFn(provider, status, extras)
    override def onProviderEnabled(provider: String) = onProviderEnabledFn(provider)
    override def onProviderDisabled(provider: String) = onProviderDisabledFn(provider)
  }

  case class OnClickListener(onClickFn: View => Unit = _ => {}) extends android.view.View.OnClickListener {

    override def onClick(view: View) = onClickFn(view)
  }

  case class OnPreferenceClassListener(onPreferenceChangeFn: (Preference, scala.Any) => Boolean)
      extends android.preference.Preference.OnPreferenceChangeListener {

    override def onPreferenceChange(preference: Preference, newValue: scala.Any) =
      onPreferenceChangeFn(preference, newValue)
  }
}
