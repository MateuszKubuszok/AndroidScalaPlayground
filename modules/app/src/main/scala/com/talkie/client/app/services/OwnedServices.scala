package com.talkie.client.app.services

import com.talkie.client.app.context.{ AppContextImpl, AppContext }
import com.talkie.client.app.navigation.{ ManualNavigationImpl, ManualNavigation, AuthNavigationImpl, AuthNavigation }
import com.talkie.client.core.context.{ CoreContextImpl, ContextImpl, CoreContext, Context }
import com.talkie.client.domain.context.{ DomainContextImpl, DomainContext }
import com.talkie.client.views.common.RichActivity

import scala.concurrent.Future

trait OwnedServices {

  def context: Context with CoreContext with DomainContext with AppContext

  // app
  def manualNavigation: ManualNavigation
  def authNavigationF: Future[AuthNavigation]
}

trait OwnedServicesImpl extends OwnedServices { self: RichActivity with SharedServicesUser =>

  lazy val context = new ContextImpl(self) with CoreContextImpl with DomainContextImpl with AppContextImpl

  // app
  lazy val manualNavigation = new ManualNavigationImpl(self)
  lazy val authNavigationF = futureService { services =>
    new AuthNavigationImpl(self, context, manualNavigation, services.facebookServices)
  }
}
