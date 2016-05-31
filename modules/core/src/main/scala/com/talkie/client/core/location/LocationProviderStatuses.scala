package com.talkie.client.core.location

import android.location.LocationProvider._

object LocationProviderStatuses extends Enumeration {
  type LocationProviderStatus = Value
  val Available = Value(AVAILABLE, "Available")
  val OutOfService = Value(OUT_OF_SERVICE, "Out of service")
  val TemporarilyUnavailable = Value(TEMPORARILY_UNAVAILABLE, "Temporarily unavailable")
  val UnknownStatus = Value(-1, "Unknown status")
}
