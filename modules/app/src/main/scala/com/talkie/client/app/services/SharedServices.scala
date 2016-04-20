package com.talkie.client.app.services

import com.facebook.FacebookSdk
import com.talkie.client.app.context.{ AppContext, AppContextImpl }
import com.talkie.client.app.navigation._
import com.talkie.client.core.context.{ Context, ContextImpl, CoreContext, CoreContextImpl }
import com.talkie.client.core.events.{ EventBusImpl, EventBus }
import com.talkie.client.core.permissions.{ PermissionsServicesImpl, PermissionsServices }
import com.talkie.client.core.scheduler.{ SchedulerImpl, Scheduler }
import com.talkie.client.domain.context.{ DomainContext, DomainContextImpl }
import com.talkie.client.domain.jobs.{ LocationJobsImpl, LocationJobs }
import com.talkie.client.domain.services.facebook.{ FacebookServicesImpl, FacebookServices }
import com.talkie.client.domain.services.location.{ LocationServicesImpl, LocationServices }
import net.danlew.android.joda.JodaTimeAndroid
import org.scaloid.common.{ LocalServiceConnection, LocalService }

import scala.concurrent.{ Future, Promise }
import scala.concurrent.duration._

trait SharedServices {

  def context: Context with CoreContext with DomainContext with AppContext

  // core
  def eventBus: EventBus
  def permissionsServices: PermissionsServices
  def scheduler: Scheduler

  // domain
  def facebookServices: FacebookServices
  def locationServices: LocationServices
  def locationJobs: LocationJobs

  // app
  def accessTokenObserver: AccessTokenObserver
}

class SharedServicesImpl extends LocalService with SharedServices {

  override lazy val context = new ContextImpl(this) with CoreContextImpl with DomainContextImpl with AppContextImpl

  private lazy val logger = context.loggerFor(this)

  // core
  override lazy val eventBus = new EventBusImpl(context)
  override lazy val permissionsServices = new PermissionsServicesImpl
  override lazy val scheduler = new SchedulerImpl(context)

  // domain
  override lazy val facebookServices = new FacebookServicesImpl(context, eventBus)
  override lazy val locationServices = new LocationServicesImpl(context, permissionsServices)
  override lazy val locationJobs = new LocationJobsImpl(context, eventBus, locationServices)

  // app
  override lazy val accessTokenObserver = new AccessTokenObserver(context, eventBus)

  onCreate {
    accessTokenObserver.configureBindings(this)

    JodaTimeAndroid.init(context.androidContext)
    logger trace "JodaTime initialized"

    FacebookSdk.sdkInitialize(context.androidContext.getApplicationContext)
    logger trace "Facebook SDK initialized"

    logger info "SharedServices created"
  }

  onDestroy {
    logger info "SharedServices stopped"
  }
}

trait SharedServicesUser { self: OwnedServices =>

  private val timeout = 30 seconds

  protected def sharedServices: LocalServiceConnection[SharedServicesImpl]

  protected def futureService[T](block: SharedServices => T): Future[T] = {
    val promise = Promise[T]()

    sharedServices { services =>
      implicit val ec = self.context.serviceExecutionContext
      promise.completeWith(Future(block(services)))
    }

    promise.future
  }
}
