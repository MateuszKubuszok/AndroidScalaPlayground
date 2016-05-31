package com.talkie.client.app.services

import com.talkie.client.app.navigation.{ NavigationServiceInterpreterImpl, NavigationServiceInterpreter }
import com.talkie.client.core.components.Activity
import com.talkie.client.core.context.Context
import com.talkie.client.core.events.{ EventServiceInterpreter, EventServiceInterpreterImpl }
import com.talkie.client.core.facebook.{ FacebookServiceInterpreter, FacebookServiceInterpreterImpl }
import com.talkie.client.core.location.{ LocationServiceInterpreterImpl, LocationServiceInterpreter }
import com.talkie.client.core.permissions.{ PermissionServiceInterpreterImpl, PermissionServiceInterpreter }
import com.talkie.client.core.scheduler.{ SchedulerServiceInterpreter, SchedulerServiceInterpreterImpl }
import com.talkie.client.core.services.{ Service, ServiceInterpreter }
import com.talkie.client.domain.tracking.{ TrackingServiceInterpreterImpl, TrackingServiceInterpreter }

import scalaz.concurrent.Task

final class ServiceInterpreterImpl(context: Context, activity: Activity) extends ServiceInterpreter {

  private val logger = context.loggerFor(this)

  // core

  val eventSI: EventServiceInterpreter = new EventServiceInterpreterImpl(context)
  val facebookSI: FacebookServiceInterpreter = new FacebookServiceInterpreterImpl(context, activity, eventSI)
  val permissionSI: PermissionServiceInterpreter = new PermissionServiceInterpreterImpl(context, activity)
  val locationSI: LocationServiceInterpreter = new LocationServiceInterpreterImpl(context, permissionSI)
  val schedulerSI: SchedulerServiceInterpreter = new SchedulerServiceInterpreterImpl(context)

  // domain

  val trackingSI: TrackingServiceInterpreter =
    new TrackingServiceInterpreterImpl(context, activity, eventSI, locationSI, schedulerSI)

  // app

  val navigationSI: NavigationServiceInterpreter =
    new NavigationServiceInterpreterImpl(context, activity, eventSI, facebookSI)

  // forService

  val unhandledService: PartialFunction[Service[Nothing], Task[Nothing]] = {
    case service: Service[_] =>
      val message = s"Unhandled service: $service"
      logger error message
      throw new IllegalStateException(message)
  }

  val forService = PartialFunction.empty
    .orElse(eventSI.forService)
    .orElse(facebookSI.forService)
    .orElse(permissionSI.forService)
    .orElse(locationSI.forService)
    .orElse(schedulerSI.forService)
    .orElse(trackingSI.forService)
    .orElse(navigationSI.forService)
    .orElse(unhandledService)

  def apply[R](in: Service[R]): Task[R] = forService.apply(in.asInstanceOf[Service[Nothing]]).asInstanceOf[Task[R]]
}
