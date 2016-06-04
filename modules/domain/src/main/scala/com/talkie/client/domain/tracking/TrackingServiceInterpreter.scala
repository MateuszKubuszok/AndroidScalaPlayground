package com.talkie.client.domain.tracking

import com.talkie.client.common.components.Activity
import com.talkie.client.common.context.Context
import com.talkie.client.common.services.{ ~@~>, ~&~> }
import com.talkie.client.core.events.EventServiceInterpreter
import com.talkie.client.core.location.LocationServiceInterpreter
import com.talkie.client.core.scheduler.SchedulerServiceInterpreter

import scalaz.concurrent.Task

trait TrackingServiceInterpreter extends (TrackingService ~@~> Task)

object TrackingServiceInterpreter extends (TrackingService ~&~> Task)

final class TrackingServiceInterpreterImpl(
    context:     Context,
    requestor:   Activity,
    eventSI:     EventServiceInterpreter,
    locationSI:  LocationServiceInterpreter,
    schedulerSI: SchedulerServiceInterpreter
) extends TrackingServiceInterpreter {

  private val actions = new TrackingActions(context, requestor, eventSI, locationSI, schedulerSI)

  override def apply[R](in: TrackingService[R]): Task[R] = in match {

    case ConfigureTracking       => actions.configureTracking().asInstanceOf[Task[R]]

    case CheckLastLocation       => actions.checkLastLocation().asInstanceOf[Task[R]]

    case TurnOnLocationTracking  => actions.turnOnLocationTracking().asInstanceOf[Task[R]]

    case TurnOffLocationTracking => actions.turnOffLocationTracking().asInstanceOf[Task[R]]
  }
}
