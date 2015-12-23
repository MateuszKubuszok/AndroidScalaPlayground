package com.talkie.client.services.location

import android.location.LocationManager

object LocationProvider extends Enumeration {
  type LocationProvider = Value
  val NetworkProvider = Value(LocationManager.NETWORK_PROVIDER) 
  val GpsProvider = Value(LocationManager.GPS_PROVIDER) 
  val PassiveProvider = Value(LocationManager.PASSIVE_PROVIDER) 
}
