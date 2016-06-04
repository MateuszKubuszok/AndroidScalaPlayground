package com.talkie.client.domain.tracking

import android.location.Location
import com.talkie.client.common.services.{ Service => GenericService }

import scalaz.Free

sealed trait TrackingService[R] extends GenericService[R]
case object ConfigureTracking extends TrackingService[Unit]
case object CheckLastLocation extends TrackingService[Option[Location]]
case object TurnOnLocationTracking extends TrackingService[Boolean]
case object TurnOffLocationTracking extends TrackingService[Boolean]

trait TrackingServiceFrees[S[R] >: TrackingService[R]] {

  def configureTracking: Free[S, Unit] =
    Free.liftF(ConfigureTracking: S[Unit])

  def checkLastLocationJob: Free[S, Option[Location]] =
    Free.liftF(CheckLastLocation: S[Option[Location]])

  def turnOnLocationTrackingJob: Free[S, Boolean] =
    Free.liftF(TurnOnLocationTracking: S[Boolean])

  def turnOffLocationTrackingJob: Free[S, Boolean] =
    Free.liftF(TurnOffLocationTracking: S[Boolean])
}

object TrackingService extends TrackingServiceFrees[TrackingService]
object Service extends TrackingServiceFrees[GenericService]
