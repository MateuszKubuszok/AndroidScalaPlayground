package com.talkie.client.domain.jobs

import android.app.Activity
import android.location.{ Location, LocationListener }
import android.os.Bundle
import com.talkie.client.core.context.{ Context, CoreContext }
import com.talkie.client.core.events.EventBus
import com.talkie.client.core.events.EventMessages.NotifyEventListenersRequest
import com.talkie.client.core.scheduler.Job
import com.talkie.client.core.scheduler.SchedulerMessages.SchedulePeriodicJobRequest
import com.talkie.client.domain.events.LocationEvents.{ LocationChangedEvent, ProviderStatusChangedEvent, ProviderDisabled, ProviderEnabled }
import com.talkie.client.domain.services.location.LocationMessages.{ LastKnownLocationRequest, RemoveLocationListenerRequest, RegisterLocationListenerRequest }
import com.talkie.client.domain.services.location.{ LocationProvider, LocationServices, ProviderStatus }

import scala.concurrent.duration._

trait LocationJobs {

  def turnOnLocationTrackingJob(requestor: Activity): SchedulePeriodicJobRequest
  def turnOffLocationTrackingJob(requestor: Activity): SchedulePeriodicJobRequest
  def checkLastLocationJob(requestor: Activity): SchedulePeriodicJobRequest
}

class LocationJobsImpl(
    context:          Context with CoreContext,
    eventBus:         EventBus,
    locationServices: LocationServices
) extends LocationJobs {

  override def turnOnLocationTrackingJob(requestor: Activity) =
    SchedulePeriodicJobRequest(TurnOnLocationTracking(requestor), Duration.Zero, 2 minutes)
  override def turnOffLocationTrackingJob(requestor: Activity) =
    SchedulePeriodicJobRequest(TurnOffLocationTracking(requestor), 1 minutes, 2 minutes)
  override def checkLastLocationJob(requestor: Activity) =
    SchedulePeriodicJobRequest(CheckLastLocation(requestor), 90 seconds, 1 minute)

  private implicit val c = context

  private val logger = context.loggerFor(this)

  private val providers = Set(LocationProvider.GpsProvider, LocationProvider.NetworkProvider, LocationProvider.PassiveProvider) // replace with settings

  private object LocationEventNotifier extends LocationListener {

    override def onProviderEnabled(provider: String) =
      eventBus.notifyEventListeners(NotifyEventListenersRequest(ProviderEnabled(provider)))

    override def onProviderDisabled(provider: String) =
      eventBus.notifyEventListeners(NotifyEventListenersRequest(ProviderDisabled(provider)))

    override def onStatusChanged(provider: String, status: Int, extras: Bundle) =
      eventBus.notifyEventListeners(NotifyEventListenersRequest(
        ProviderStatusChangedEvent(provider, ProviderStatus.values.find(_.id == status).get)
      ))

    override def onLocationChanged(location: Location) =
      eventBus.notifyEventListeners(NotifyEventListenersRequest(LocationChangedEvent(location)))
  }

  private case class TurnOnLocationTracking(requestor: Activity) extends Job {

    override def run() = locationServices.registerLocationListener(
      RegisterLocationListenerRequest(requestor, LocationEventNotifier, providers)
    )
  }

  private case class TurnOffLocationTracking(requestor: Activity) extends Job {

    override def run() = locationServices.removeLocationListener(RemoveLocationListenerRequest(LocationEventNotifier))
  }

  private case class CheckLastLocation(requestor: Activity) extends Job {

    override def run() = {
      implicit val ec = context.serviceExecutionContext
      for {
        gpsResult <- locationServices.getLastKnownLocation(
          LastKnownLocationRequest(requestor, LocationProvider.GpsProvider)
        )
        networkResult <- locationServices.getLastKnownLocation(
          LastKnownLocationRequest(requestor, LocationProvider.NetworkProvider)
        )
        passiveResult <- locationServices.getLastKnownLocation(
          LastKnownLocationRequest(requestor, LocationProvider.PassiveProvider)
        )
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
