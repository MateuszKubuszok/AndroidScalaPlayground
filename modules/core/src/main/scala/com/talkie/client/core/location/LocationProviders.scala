package com.talkie.client.core.location

import android.location.LocationManager._

object LocationProviders extends Enumeration {
  type LocationProvider = Value
  val NetworkProvider = Value(NETWORK_PROVIDER)
  val GpsProvider = Value(GPS_PROVIDER)
  val PassiveProvider = Value(PASSIVE_PROVIDER)
}
