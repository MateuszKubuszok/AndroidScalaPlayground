package com.talkie.client.domain.services.location

import android.app.Activity
import android.location.{ Location, LocationListener }
import com.talkie.client.domain.services.location.LocationProvider.LocationProvider

object LocationMessages {

  case class LastKnownLocationRequest(requestor: Activity, provider: LocationProvider)
  case class LastKnownLocationResponse(locationOpt: Option[Location])

  case class RegisterLocationListenerRequest(
    requestor: Activity,
    listener:  LocationListener,
    providers: Set[LocationProvider]
  )
  case class RegisterLocationListenerResponse(registered: Boolean)

  case class RemoveLocationListenerRequest(listener: LocationListener)
  case class RemoveLocationListenerResponse()
}
