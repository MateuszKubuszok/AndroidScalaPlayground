package com.talkie.client.app.activities.common

import com.talkie.client.app.services._
import com.talkie.client.core.events.{ Event, EventBus }
import com.talkie.client.core.events.EventMessages.{ RemoveEventListenerRequest, RegisterEventListenerRequest }
import com.talkie.client.core.scheduler.Scheduler
import com.talkie.client.core.scheduler.SchedulerMessages.{ SchedulePeriodicJobRequest, CancelJobRequest }
import com.talkie.client.views.TypedFindView
import com.talkie.client.views.common.RichActivity
import com.talkie.client.views.common.views.TypedFindLayout
import org.scaloid.common.LocalServiceConnection

import scala.concurrent.Future

trait Controller extends TypedFindView with TypedFindLayout with SharedServicesUser with OwnedServices {

  private implicit val ec = context.serviceExecutionContext

  def asyncAction[T](action: => T): Unit = Future(action)

  def asyncAction[T](action: Future[T]): Unit = {}
}

trait ControllerImpl extends Controller with OwnedServicesImpl {
  self: RichActivity =>

  private implicit val c = context
  private implicit val ec = context.serviceExecutionContext

  private val logger = context.loggerFor(this)

  override val sharedServices = new LocalServiceConnection[SharedServicesImpl]

  sharedServices { services =>
    import services._

    accessTokenObserver.configureBindings(this)

    scheduleForLifetime(scheduler, locationJobs.turnOnLocationTrackingJob(this), canInterrupt = true)
    scheduleForLifetime(scheduler, locationJobs.turnOffLocationTrackingJob(this), canInterrupt = false)
    scheduleForLifetime(scheduler, locationJobs.checkLastLocationJob(this), canInterrupt = true)
    logger trace "Location initialized"

    authNavigationF map { authNavigation =>
      registerForLifetime(eventBus, RegisterEventListenerRequest(authNavigation.onUserLoggedIn))
      registerForLifetime(eventBus, RegisterEventListenerRequest(authNavigation.onUserLoggedOut))
      logger trace "Log in/out event listeners registered"
    }
  }

  private def scheduleForLifetime(scheduler: Scheduler, request: SchedulePeriodicJobRequest, canInterrupt: Boolean) {
    scheduler.schedulePeriodicJob(request)
    onDestroy {
      scheduler.cancelJob(CancelJobRequest(request.job, canInterrupt = canInterrupt))
    }
  }

  private def registerForLifetime[E <: Event](eventBus: EventBus, request: RegisterEventListenerRequest[E]) {
    eventBus.registerEventListener(request)
    onDestroy {
      eventBus.removeEventListener(RemoveEventListenerRequest(request.listener)(request.eventType))
    }
  }
}
