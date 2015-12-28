package com.talkie.client.domain.events

import android.location.Location
import com.talkie.client.core.events.Event
import com.talkie.client.domain.services.location.ProviderStatus.ProviderStatus

object LocationEvents {

  case class ProviderEnabled(provider: String) extends Event {

    override type Details = String

    override def getDetails: Details = provider
  }

  case class ProviderDisabled(provider: String) extends Event {

    override type Details = String

    override def getDetails: Details = provider
  }

  case class ProviderStatusChangedEvent(provider: String, status: ProviderStatus) extends Event {

    override type Details = (String, ProviderStatus)

    override lazy val getDetails: Details = (provider, status)
  }

  case class LocationChangedEvent(location: Location) extends Event {

    override type Details = Location

    override lazy val getDetails: Details = location
  }
}
