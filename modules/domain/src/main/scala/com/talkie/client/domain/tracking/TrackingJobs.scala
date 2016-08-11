package com.talkie.client.domain.tracking

import android.app.job.{ JobParameters, JobService }
import android.location.{ Location, LocationListener }
import android.os.Bundle

import com.talkie.client.common.components.{ Service => ComponentService }
import com.talkie.client.core.events.{ EventActions, EventActionsImpl }
import com.talkie.client.core.location._
import com.talkie.client.core.permissions.{ PermissionActions, PermissionActionsImpl }

trait TrackingJob extends JobService with ComponentService {

  implicit val eventActions: EventActions = new EventActionsImpl
  implicit val permissionActions: PermissionActions = new PermissionActionsImpl
  implicit val locationActions: LocationActions = new LocationActionsImpl

  val providers = LocationProviders.values.toSeq // replace with settings

  object LocationEventNotifier extends LocationListener {

    override def onProviderEnabled(provider: String) =
      eventActions.notifyEventListeners(ProviderEnabled(provider))

    override def onProviderDisabled(provider: String) =
      eventActions.notifyEventListeners(ProviderDisabled(provider))

    override def onStatusChanged(provider: String, status: Int, extras: Bundle) =
      eventActions.notifyEventListeners(
        ProviderStatusChangedEvent(
          provider,
          LocationProviderStatuses.values.find(_.id == status) getOrElse LocationProviderStatuses.UnknownStatus
        )
      )

    override def onLocationChanged(location: Location) =
      eventActions.notifyEventListeners(LocationChangedEvent(location))
  }
}

class CheckLastKnownLocationJob extends TrackingJob {

  override def onStartJob(params: JobParameters): Boolean = {
    val locationOpt = locationActions.checkLastKnownLocation()
    logger info s"Last known location $locationOpt"
    false
  }

  override def onStopJob(params: JobParameters): Boolean = true
}

class TurnOnLocationTrackingJob extends TrackingJob {

  override def onStartJob(params: JobParameters): Boolean = {
    locationActions.registerLocationListener(LocationEventNotifier, providers: _*)
    false
  }

  override def onStopJob(params: JobParameters): Boolean = true
}

class TurnOffLocationTrackingJob extends TrackingJob {

  override def onStartJob(params: JobParameters): Boolean = {
    locationActions.removeLocationListener(LocationEventNotifier)
    false
  }

  override def onStopJob(params: JobParameters): Boolean = true
}
