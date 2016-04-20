package com.talkie.client.app.services

import com.talkie.client.app.context.{ AppContextImpl, AppContext }
import com.talkie.client.app.navigation._
import com.talkie.client.core.context.{ CoreContextImpl, ContextImpl, CoreContext, Context }
import com.talkie.client.domain.context.{ DomainContextImpl, DomainContext }
import com.talkie.client.views.common.RichActivity

import scala.concurrent.Future

trait OwnedServices {

  def context: Context with CoreContext with DomainContext with AppContext

  // app
  def manualNavigation: ManualNavigation
  def authNavigationF: Future[AuthNavigation]
  def accessTokenObserverF: Future[AccessTokenObserver]
}

trait OwnedServicesImpl extends OwnedServices { self: RichActivity with SharedServicesUser =>

  override lazy val context = new ContextImpl(self) with CoreContextImpl with DomainContextImpl with AppContextImpl

  // app
  override lazy val manualNavigation = new ManualNavigationImpl(self)
  override lazy val authNavigationF = futureService { services =>
    new AuthNavigationImpl(self, context, manualNavigation, services.facebookServices)
  }
  override lazy val accessTokenObserverF = futureService { services =>
    new AccessTokenObserver(context, services.eventBus)
  }
}
