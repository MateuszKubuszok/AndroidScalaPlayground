package com.talkie.client.domain.tracking

import android.location.Location
import com.talkie.client.common.events.Event
import com.talkie.client.core.location.LocationProviderStatuses.LocationProviderStatus

case class ProviderEnabled(provider: String) extends Event {

  override type Details = String

  override def getDetails: Details = provider
}

case class ProviderDisabled(provider: String) extends Event {

  override type Details = String

  override def getDetails: Details = provider
}

case class ProviderStatusChangedEvent(provider: String, status: LocationProviderStatus) extends Event {

  override type Details = (String, LocationProviderStatus)

  override lazy val getDetails: Details = (provider, status)
}

case class LocationChangedEvent(location: Location) extends Event {

  override type Details = Location

  override lazy val getDetails: Details = location
}
