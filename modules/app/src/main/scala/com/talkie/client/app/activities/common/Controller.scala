package com.talkie.client.app.activities.common

import com.talkie.client.app.services._
import com.talkie.client.core.events.EventMessages.RegisterEventListenerRequest
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

  private val logger = context.loggerFor(this)

  override val sharedServices = new LocalServiceConnection[SharedServicesImpl]

  sharedServices { services =>
    implicit val c = context
    implicit val ec = context.serviceExecutionContext

    services.accessTokenObserver.configureBindings(this)

    services.scheduler.schedulePeriodicJob(services.locationJobs.turnOnLocationTrackingJob(this))
    services.scheduler.schedulePeriodicJob(services.locationJobs.turnOffLocationTrackingJob(this))
    services.scheduler.schedulePeriodicJob(services.locationJobs.checkLastLocationJob(this))
    logger trace "Location initialized"

    authNavigationF map { authNavigation =>
      services.eventBus.registerEventListener(RegisterEventListenerRequest(authNavigation.onUserLoggedIn))
      services.eventBus.registerEventListener(RegisterEventListenerRequest(authNavigation.onUserLoggedOut))
      logger trace "Log in/out event listeners registered"
    }
  }
}
