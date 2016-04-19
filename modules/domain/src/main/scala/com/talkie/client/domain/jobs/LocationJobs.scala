package com.talkie.client.domain.jobs

import android.location.{ Location, LocationListener }
import android.os.Bundle
import com.talkie.client.core.events.EventBus
import com.talkie.client.core.events.EventMessages.NotifyEventListenersRequest
import com.talkie.client.core.scheduler.Job
import com.talkie.client.core.scheduler.SchedulerMessages.SchedulePeriodicJobRequest
import com.talkie.client.core.services.Context
import com.talkie.client.domain.events.LocationEvents.{ LocationChangedEvent, ProviderStatusChangedEvent, ProviderDisabled, ProviderEnabled }
import com.talkie.client.domain.services.location.LocationMessages.{ LastKnownLocationRequest, RemoveLocationListenerRequest, RegisterLocationListenerRequest }
import com.talkie.client.domain.services.location.{ LocationProvider, LocationServices, ProviderStatus }

import scala.concurrent.duration._

trait LocationJobs {

  def turnOnLocationTrackingJob: SchedulePeriodicJobRequest
  def turnOffLocationTrackingJob: SchedulePeriodicJobRequest
  def checkLastLocationJob: SchedulePeriodicJobRequest
}

class LocationJobsImpl(context: Context, eventBus: EventBus, locationServices: LocationServices) extends LocationJobs {

  override val turnOnLocationTrackingJob = SchedulePeriodicJobRequest(TurnOnLocationTracking, Duration.Zero, 2 minutes)
  override val turnOffLocationTrackingJob = SchedulePeriodicJobRequest(TurnOffLocationTracking, 1 minutes, 2 minutes)
  override val checkLastLocationJob = SchedulePeriodicJobRequest(CheckLastLocation, 90 seconds, 1 minute)

  private implicit val c = context

  private val logger = context.loggerFor(this)

  private val providers = Set(LocationProvider.GpsProvider, LocationProvider.NetworkProvider, LocationProvider.PassiveProvider) // replace with settings

  private object LocationEventNotifier extends LocationListener {

    override def onProviderEnabled(provider: String) =
      eventBus.notifyEventListeners(NotifyEventListenersRequest(ProviderEnabled(provider)))

    override def onProviderDisabled(provider: String) =
      eventBus.notifyEventListeners(NotifyEventListenersRequest(ProviderDisabled(provider)))

    override def onStatusChanged(provider: String, status: Int, extras: Bundle) =
      eventBus.notifyEventListeners(NotifyEventListenersRequest(ProviderStatusChangedEvent(provider, ProviderStatus.values.find(_.id == status).get)))

    override def onLocationChanged(location: Location) =
      eventBus.notifyEventListeners(NotifyEventListenersRequest(LocationChangedEvent(location)))
  }

  private object TurnOnLocationTracking extends Job {

    override def run() = locationServices.registerLocationListener(RegisterLocationListenerRequest(LocationEventNotifier, providers))
  }

  private object TurnOffLocationTracking extends Job {

    override def run() = locationServices.removeLocationListener(RemoveLocationListenerRequest(LocationEventNotifier))
  }

  private object CheckLastLocation extends Job {

    override def run() = {
      implicit val ec = context.serviceExecutionContext
      for {
        gpsResult <- locationServices.getLastKnownLocation(LastKnownLocationRequest(LocationProvider.GpsProvider))
        networkResult <- locationServices.getLastKnownLocation(LastKnownLocationRequest(LocationProvider.NetworkProvider))
        passiveResult <- locationServices.getLastKnownLocation(LastKnownLocationRequest(LocationProvider.PassiveProvider))
      } {
        logger info "Last known location:\n" +
          s" - by GPS (${gpsResult.locationOpt}),\n" +
          s" - by Network (${networkResult.locationOpt})\n" +
          s" - by Passive (${passiveResult.locationOpt})"

        // force data update as LocationListener seems to be rather lazy
      }
    }
  }
}
