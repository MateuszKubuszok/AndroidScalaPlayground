package com.talkie.client.domain.facebook

import com.facebook.Profile
import com.talkie.client.domain.facebook.FacebookMessages._
import com.talkie.client.services.{ Service, SyncService }

trait FacebookServices {

  def checkIfLogged: SyncService[CheckLoggedStatusRequest, CheckLoggedStatusResponse]
}

trait FacebookServicesComponent {

  def facebookServices: FacebookServices
}

trait FacebookServicesComponentImpl extends FacebookServicesComponent {

  object facebookServices extends FacebookServices {

    override val checkIfLogged = Service { request: CheckLoggedStatusRequest =>
      CheckLoggedStatusResponse(Option(Profile.getCurrentProfile).isDefined)
    }
  }
}