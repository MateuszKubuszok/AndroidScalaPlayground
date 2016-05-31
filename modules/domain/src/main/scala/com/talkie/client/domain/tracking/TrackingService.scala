package com.talkie.client.domain.tracking

import android.location.Location
import com.talkie.client.core.services.Service

import scalaz.Free

sealed trait TrackingService[R] extends Service[R]
case object ConfigureTracking extends TrackingService[Unit]
case object CheckLastLocation extends TrackingService[Option[Location]]
case object TurnOnLocationTracking extends TrackingService[Boolean]
case object TurnOffLocationTracking extends TrackingService[Boolean]

object TrackingService {

  def configureTracking: Free[TrackingService, Unit] =
    Free.liftF(ConfigureTracking: TrackingService[Unit])

  def checkLastLocationJob: Free[TrackingService, Option[Location]] =
    Free.liftF(CheckLastLocation: TrackingService[Option[Location]])

  def turnOnLocationTrackingJobs: Free[TrackingService, Boolean] =
    Free.liftF(TurnOnLocationTracking: TrackingService[Boolean])

  def turnOffLocationTrackingJobs: Free[TrackingService, Boolean] =
    Free.liftF(TurnOffLocationTracking: TrackingService[Boolean])
}

