package com.talkie.client.domain.services.location

import android.location.LocationProvider._

object ProviderStatus extends Enumeration {
  type ProviderStatus = Value
  val Available = Value(AVAILABLE, "Available")
  val OutOfService = Value(OUT_OF_SERVICE, "Out of service")
  val TemporarilyUnavailable = Value(TEMPORARILY_UNAVAILABLE, "Temporarily unavailable")
}
