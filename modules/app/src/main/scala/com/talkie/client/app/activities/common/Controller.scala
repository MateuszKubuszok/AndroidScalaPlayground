package com.talkie.client.app.activities.common

import com.talkie.client.app.navigation._
import com.talkie.client.core.events.{ EventBus, EventBusImpl }
import com.talkie.client.core.permissions.{ PermissionsServices, PermissionsServicesImpl }
import com.talkie.client.core.scheduler.{ Scheduler, SchedulerImpl }
import com.talkie.client.core.services.Context
import com.talkie.client.domain.jobs.{ LocationJobs, LocationJobsImpl }
import com.talkie.client.domain.services.facebook.{ FacebookServices, FacebookServicesImpl }
import com.talkie.client.domain.services.location.{ LocationServicesImpl, LocationServices }
import com.talkie.client.views.TypedFindView
import com.talkie.client.views.common.RichActivity
import com.talkie.client.views.common.views.TypedFindLayout

import scala.concurrent.Future

trait Controller extends TypedFindView with TypedFindLayout {

  protected val context: Context

  // core
  protected val eventBus: EventBus
  protected val permissionsServices: PermissionsServices
  protected val scheduler: Scheduler

  // domain
  protected val facebookServices: FacebookServices
  protected val locationServices: LocationServices
  protected val locationJobs: LocationJobs

  // app
  protected val accessTokenObserver: AccessTokenObserver
  protected val manualNavigation: ManualNavigation
  protected val authNavigation: AuthNavigation

  private implicit val ec = context.serviceExecutionContext

  def asyncAction[T](action: => T): Unit = Future(action)

  def asyncAction[T](action: Future[T]): Unit = {}
}

trait ControllerImpl extends Controller {
  self: RichActivity =>

  lazy val context = Context.from(self)

  // core
  lazy val eventBus = new EventBusImpl
  lazy val permissionsServices = new PermissionsServicesImpl
  lazy val scheduler = new SchedulerImpl(context)

  // domain
  lazy val facebookServices = new FacebookServicesImpl(context, eventBus)
  lazy val locationServices = new LocationServicesImpl(context, permissionsServices)
  lazy val locationJobs = new LocationJobsImpl(context, eventBus, locationServices)

  // app
  lazy val accessTokenObserver = new AccessTokenObserver(self, context, eventBus)
  lazy val manualNavigation = new ManualNavigationImpl(self)
  lazy val authNavigation = new AuthNavigationImpl(self, context, manualNavigation, facebookServices, accessTokenObserver)
}
