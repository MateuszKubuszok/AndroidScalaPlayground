package com.talkie.client.domain.tracking

import android.location.{ Location, LocationListener }
import android.os.Bundle
import com.talkie.client.common.components.Activity
import com.talkie.client.common.context.Context
import com.talkie.client.common.scheduler.Job
import com.talkie.client.common.services.ServiceInterpreter
import com.talkie.client.core.events.EventService._
import com.talkie.client.core.events.EventServiceInterpreter
import com.talkie.client.core.location.LocationService._
import com.talkie.client.core.location.{ LocationProviders, LocationProviderStatuses, LocationServiceInterpreter }
import com.talkie.client.core.scheduler.SchedulerService._
import com.talkie.client.core.scheduler.SchedulerServiceInterpreter

import scala.concurrent.duration._
import scalaz.concurrent.Task

private[tracking] final class TrackingActions(
    context:                  Context,
    requestor:                Activity,
    implicit val eventSI:     EventServiceInterpreter,
    implicit val locationSI:  LocationServiceInterpreter,
    implicit val schedulerSI: SchedulerServiceInterpreter
) {

  private val logger = context.loggerFor(this)

  private val providers = LocationProviders.values.toSeq // replace with settings

  def configureTracking(): Task[Unit] = Task {
    import ServiceInterpreter.TaskRunner
    import SchedulerServiceInterpreter._

    requestor.bootstrap {
      (for {
        _ <- turnOnLocationTracking()
        _ <- turnOffLocationTracking()
        _ <- checkLastLocation()
      } yield ()).fireAndWait()
    }

    requestor.teardown {
      (for {
        _ <- cancelJob(TurnOnLocationTrackingJob, canInterrupt = true)
        _ <- cancelJob(TurnOffLocationTrackingJob, canInterrupt = false)
        _ <- cancelJob(CheckLastKnownLocationJob, canInterrupt = true)
      } yield ()).fireAndWait()
    }
  }

  def checkLastLocation(): Task[Job] = {
    import SchedulerServiceInterpreter._

    (for {
      isSuccess <- schedulePeriodicJob(CheckLastKnownLocationJob, 90 seconds, 1 minute)
    } yield CheckLastKnownLocationJob).interpret
  }

  def turnOnLocationTracking(): Task[Job] = {
    import SchedulerServiceInterpreter._

    (for {
      isSuccess <- schedulePeriodicJob(TurnOnLocationTrackingJob, Duration.Zero, 2 minutes)
    } yield TurnOnLocationTrackingJob).interpret
  }

  def turnOffLocationTracking(): Task[Job] = {
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
