package com.talkie.client.core.permissions

import android.Manifest.permission._

object RequiredPermissions extends Enumeration {
  type RequiredPermissions = Value
  // For Facebook SDK
  val Internet = Value(INTERNET)
  // For GPS and WiFi location services
  val AccessFineLocation = Value(ACCESS_FINE_LOCATION)
}
