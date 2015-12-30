package com.talkie.client.core.initialization

import android.location.{Location, LocationListener}
import android.os.Bundle
import com.talkie.client.core.events.EventBusComponent
import com.talkie.client.core.events.EventMessages.NotifyEventListenersRequest
import com.talkie.client.core.logging.LoggerComponent
import com.talkie.client.core.scheduler.SchedulerMessages.SchedulePeriodicJobRequest
import com.talkie.client.core.scheduler.{Job, SchedulerComponent}
import com.talkie.client.core.services.ContextComponent
import com.talkie.client.domain.events.LocationEvents._
import com.talkie.client.domain.services.location.LocationMessages._
import com.talkie.client.domain.services.location.{LocationProvider, LocationServicesComponent, ProviderStatus}

import scala.concurrent.duration._

private[initialization] trait LocationInitializer extends Initialization {
  self: ContextComponent
    with EventBusComponent
    with LoggerComponent
    with SchedulerComponent
    with LocationServicesComponent =>

  implicit val c = context

  val notifier = new LocationEventNotifier
  val providers = Set(LocationProvider.GpsProvider, LocationProvider.NetworkProvider, LocationProvider.PassiveProvider) // replace with settings

  val turnOn = new TurnOnLocationTracking
  val turnOff = new TurnOffLocationTracking
  val checkLocation = new CheckLocation

  override def initialize() {
    super.initialize()
    scheduler.schedulePeriodicJob(SchedulePeriodicJobRequest(turnOn, Duration.Zero, 10 minutes))
    scheduler.schedulePeriodicJob(SchedulePeriodicJobRequest(turnOff, 5 minutes, 10 minutes))
//    scheduler.schedulePeriodicJob(SchedulePeriodicJobRequest(checkLocation, 2 seconds, 1 minute)) // temp debug tool
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

  class CheckLocation extends Job {

    override def run() = {
      implicit val ec = context.executionContext
      for {
        gpsResult <- locationServices.getLastKnownLocation(LastKnownLocationRequest(LocationProvider.GpsProvider))
        networkResult <- locationServices.getLastKnownLocation(LastKnownLocationRequest(LocationProvider.NetworkProvider))
        passiveResult <- locationServices.getLastKnownLocation(LastKnownLocationRequest(LocationProvider.PassiveProvider))
      } logger info "Last known location:\n" +
        s" - by GPS (${gpsResult.locationOpt}),\n" +
        s" - by Network (${networkResult.locationOpt})\n" +
        s" - by Passive (${passiveResult.locationOpt})"
    }
  }
}
