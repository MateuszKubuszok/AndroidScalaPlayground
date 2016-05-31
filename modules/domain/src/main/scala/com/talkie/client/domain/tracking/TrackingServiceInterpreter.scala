package com.talkie.client.domain.tracking

import android.location.{ Location, LocationListener }
import android.os.Bundle
import com.talkie.client.core.components.Activity
import com.talkie.client.core.context.Context
import com.talkie.client.core.events.EventService._
import com.talkie.client.core.events.EventServiceInterpreter
import com.talkie.client.core.location.LocationService._
import com.talkie.client.core.location.{ LocationProviders, LocationProviderStatuses, LocationServiceInterpreter }
import com.talkie.client.core.scheduler.SchedulerService._
import com.talkie.client.core.scheduler.{ Job, SchedulerServiceInterpreter }
import com.talkie.client.core.services.{ ~@~>, ~&~> }
import com.talkie.client.core.services.ServiceInterpreter.TaskRunner

import scala.concurrent.duration._
import scalaz.concurrent.Task

trait TrackingServiceInterpreter extends (TrackingService ~@~> Task)

object TrackingServiceInterpreter extends (TrackingService ~&~> Task)

final class TrackingServiceInterpreterImpl(
    context:                  Context,
    requestor:                Activity,
    implicit val eventSI:     EventServiceInterpreter,
    implicit val locationSI:  LocationServiceInterpreter,
    implicit val schedulerSI: SchedulerServiceInterpreter
) extends TrackingServiceInterpreter {

  private val logger = context.loggerFor(this)

  private val providers = LocationProviders.values.toSeq // replace with settings

  override def apply[R](in: TrackingService[R]): Task[R] = in match {

    case ConfigureTracking       => configureTracking().asInstanceOf[Task[R]]

    case CheckLastLocation       => checkLastLocation().asInstanceOf[Task[R]]

    case TurnOnLocationTracking  => turnOnLocationTracking().asInstanceOf[Task[R]]

    case TurnOffLocationTracking => turnOffLocationTracking().asInstanceOf[Task[R]]
  }

  private def configureTracking(): Task[Unit] = Task {
    import SchedulerServiceInterpreter._

    requestor.bootstrap {
      (for {
        _ <- turnOnLocationTracking()
        _ <- turnOffLocationTracking()
        _ <- checkLastLocation()
      } yield ()).fireAndForget()
    }

    requestor.teardown {
      (for {
        _ <- cancelJob(TurnOnLocationTrackingJob, canInterrupt = true)
        _ <- cancelJob(TurnOffLocationTrackingJob, canInterrupt = false)
        _ <- cancelJob(CheckLastKnownLocationJob, canInterrupt = true)
      } yield ()).fireAndForget()
    }
  }

  private def checkLastLocation(): Task[Job] = {
    import SchedulerServiceInterpreter._

    (for {
      isSuccess <- schedulePeriodicJob(CheckLastKnownLocationJob, 90 seconds, 1 minute)
    } yield CheckLastKnownLocationJob).interpret
  }

  private def turnOnLocationTracking(): Task[Job] = {
    import SchedulerServiceInterpreter._

    (for {
      isSuccess <- schedulePeriodicJob(TurnOnLocationTrackingJob, Duration.Zero, 2 minutes)
    } yield TurnOnLocationTrackingJob).interpret
  }

  private def turnOffLocationTracking(): Task[Job] = {
    import SchedulerServiceInterpreter._

    (for {
      isSuccess <- schedulePeriodicJob(TurnOffLocationTrackingJob, 1 minute, 2 minutes)
    } yield TurnOffLocationTrackingJob).interpret
  }

  private object LocationEventNotifier extends LocationListener {

    import EventServiceInterpreter._

    override def onProviderEnabled(provider: String) =
      notifyEventListeners(ProviderEnabled(provider)).fireAndForget()

    override def onProviderDisabled(provider: String) =
      notifyEventListeners(ProviderDisabled(provider)).fireAndForget()

    override def onStatusChanged(provider: String, status: Int, extras: Bundle) =
      notifyEventListeners(
        ProviderStatusChangedEvent(
          provider,
          LocationProviderStatuses.values.find(_.id == status) getOrElse LocationProviderStatuses.UnknownStatus
        )
      ).fireAndForget()

    override def onLocationChanged(location: Location) =
      notifyEventListeners(LocationChangedEvent(location)).fireAndForget()
  }

  private object TurnOnLocationTrackingJob extends Job {

    import LocationServiceInterpreter._

    override def run() = registerLocationListener(LocationEventNotifier, providers: _*).fireAndForget()
  }

  private object TurnOffLocationTrackingJob extends Job {

    import LocationServiceInterpreter._

    override def run() = removeLocationListener(LocationEventNotifier).fireAndForget()
  }

  private object CheckLastKnownLocationJob extends Job {

    import LocationServiceInterpreter._

    override def run() =
      (for {
        locationOpt <- checkLastKnownLocation(providers: _*)
      } yield {
        logger info s"Last known location $locationOpt"
        // force data update as LocationListener seems to be rather lazy
      }).fireAndForget()
  }
}
