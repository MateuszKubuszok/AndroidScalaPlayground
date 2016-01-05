package com.talkie.client.domain.services.location

import android.location.{ Location, LocationListener }
import com.talkie.client.domain.services.location.LocationProvider.LocationProvider

object LocationMessages {

  case class LastKnownLocationRequest(provider: LocationProvider)
  case class LastKnownLocationResponse(locationOpt: Option[Location])

  case class RegisterLocationListenerRequest(
    listener:  LocationListener,
    providers: Set[LocationProvider]
  )
  case class RegisterLocationListenerResponse(registered: Boolean)

  case class RemoveLocationListenerRequest(listener: LocationListener)
  case class RemoveLocationListenerResponse()
}
