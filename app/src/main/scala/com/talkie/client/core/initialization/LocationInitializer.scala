package com.talkie.client.core.initialization

import android.location.{Location, LocationListener}
import android.os.Bundle
import com.talkie.client.core.events.EventBusComponent
import com.talkie.client.core.events.EventMessages.NotifyEventListenersRequest
import com.talkie.client.core.scheduler.SchedulerMessages.SchedulePeriodicJobRequest
import com.talkie.client.core.scheduler.{Job, SchedulerComponent}
import com.talkie.client.core.services.ContextComponent
import com.talkie.client.domain.events.LocationEvents._
import com.talkie.client.domain.services.location.LocationMessages.{RemoveLocationListenerRequest, RegisterLocationListenerRequest}
import com.talkie.client.domain.services.location.{LocationProvider, LocationServicesComponent, ProviderStatus}

import scala.concurrent.duration._

trait LocationInitializer extends Initialization {
  self: ContextComponent
    with EventBusComponent
    with SchedulerComponent
    with LocationServicesComponent =>

  implicit val c = context

  val notifier = new LocationEventNotifier
  val providers = Set(LocationProvider.GpsProvider, LocationProvider.NetworkProvider) // replace by settings

  val turnOn = new TurnOnLocationTracking
  val turnOff = new TurnOffLocationTracking

  override def initialize() {
    super.initialize()
    scheduler.schedulePeriodicJob(SchedulePeriodicJobRequest(turnOn, Duration.Zero, 10 minutes))
    scheduler.schedulePeriodicJob(SchedulePeriodicJobRequest(turnOff, 5 minutes, 10 minutes))
  }

  class LocationEventNotifier extends LocationListener {

    override def onProviderEnabled(provider: String) =
      eventBus.notifyEventListeners(NotifyEventListenersRequest(ProviderEnabled(provider)))

    override def onProviderDisabled(provider: String) =
      eventBus.notifyEventListeners(NotifyEventListenersRequest(ProviderDisabled(provider)))

    override def onStatusChanged(provider: String, status: Int, extras: Bundle) =
      eventBus.notifyEventListeners(NotifyEventListenersRequest(ProviderStatusChangedEvent(provider, ProviderStatus.values.find(_.id == status).get)))

    override def onLocationChanged(location: Location) =
      eventBus.notifyEventListeners(NotifyEventListenersRequest(LocationChangedEvent(location)))
  }

  class TurnOnLocationTracking extends Job {

    override def run() = locationServices.registerLocationListener(RegisterLocationListenerRequest(notifier, providers))
  }

  class TurnOffLocationTracking extends Job {

    override def run() = locationServices.removeLocationListener(RemoveLocationListenerRequest(notifier))
  }
}
