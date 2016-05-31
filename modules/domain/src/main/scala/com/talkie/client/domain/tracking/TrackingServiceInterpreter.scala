package com.talkie.client.domain.tracking

import android.location.{ Location, LocationListener }
import android.os.Bundle
import com.talkie.client.core.components.Activity
import com.talkie.client.core.context.Context
import com.talkie.client.core.events.EventService._
import com.talkie.client.core.events.EventServiceInterpreter
import com.talkie.client.core.location.LocationService._
import com.talkie.client.core.location.{LocationProviders, LocationProviderStatuses, LocationServiceInterpreter}
import com.talkie.client.core.scheduler.SchedulerService._
import com.talkie.client.core.scheduler.{ Job, SchedulerServiceInterpreter }
import com.talkie.client.core.services.~@~>

import scala.concurrent.duration._
import scalaz.concurrent.Task

trait TrackingServiceInterpreter extends (TrackingService ~@~> Task)

final class TrackingServiceInterpreterImpl(
    context: Context,
    requestor: Activity,
    eventSI: EventServiceInterpreter,
    locationSI: LocationServiceInterpreter,
    schedulerSI: SchedulerServiceInterpreter
  ) extends TrackingServiceInterpreter {

  private val logger = context.loggerFor(this)

  private val providers = LocationProviders.values.toSeq // replace with settings

  override def apply[R](in: TrackingService[R]): Task[R] = in match {

    case CheckLastLocation => checkLastLocation().asInstanceOf[Task[R]]

    case TurnOnLocationTracking => turnOnLocationTracking().asInstanceOf[Task[R]]

    case TurnOffLocationTracking => turnOffLocationTracking().asInstanceOf[Task[R]]
  }

  private def configureTracking(): Task[Unit] = {
    requestor.teardown {
      (for {
        _ <- cancelJob(TurnOnLocationTrackingJob, canInterrupt = true)
        _ <- cancelJob(TurnOffLocationTrackingJob, canInterrupt = false)
        _ <- cancelJob(CheckLastKnownLocationJob, canInterrupt = true)
      } yield ()).foldMap(schedulerSI)
    }

    for {
      _ <- turnOnLocationTracking()
      _ <- turnOffLocationTracking()
      _ <- checkLastLocation()
    } yield ()
  }

  private def checkLastLocation(): Task[Job] =
    (for {
      isSuccess <- schedulePeriodicJob(CheckLastKnownLocationJob, 90 seconds, 1 minute)
    } yield CheckLastKnownLocationJob).foldMap(schedulerSI)

  private def turnOnLocationTracking(): Task[Job] =
    (for {
      isSuccess <- schedulePeriodicJob(TurnOnLocationTrackingJob, Duration.Zero, 2 minutes)
    } yield TurnOnLocationTrackingJob).foldMap(schedulerSI)

  private def turnOffLocationTracking(): Task[Job] =
    (for {
      isSuccess <- schedulePeriodicJob(TurnOffLocationTrackingJob, 1 minute, 2 minutes)
    } yield TurnOffLocationTrackingJob).foldMap(schedulerSI)

  private object LocationEventNotifier extends LocationListener {

    override def onProviderEnabled(provider: String) =
      notifyEventListeners(ProviderEnabled(provider)).foldMap(eventSI).attempt

    override def onProviderDisabled(provider: String) =
      notifyEventListeners(ProviderDisabled(provider)).foldMap(eventSI).attempt

    override def onStatusChanged(provider: String, status: Int, extras: Bundle) =
      notifyEventListeners(
        ProviderStatusChangedEvent(
          provider,
          LocationProviderStatuses.values.find(_.id == status) getOrElse LocationProviderStatuses.UnknownStatus
        )).foldMap(eventSI).attempt

    override def onLocationChanged(location: Location) =
      notifyEventListeners(LocationChangedEvent(location)).foldMap(eventSI).attempt
  }

  private object TurnOnLocationTrackingJob extends Job {

    override def run() = registerLocationListener(LocationEventNotifier, providers:_*).foldMap(locationSI)
  }

  private object TurnOffLocationTrackingJob extends Job {

    override def run() = removeLocationListener(LocationEventNotifier).foldMap(locationSI)
  }

  private object CheckLastKnownLocationJob extends Job {

    override def run() = {
      val callForAction = for {
        locationOpt <- checkLastKnownLocation(providers:_*)
      } yield {
        logger info s"Last known location $locationOpt"
        // force data update as LocationListener seems to be rather lazy
      }
      callForAction.foldMap(locationSI)
    }
  }
}
