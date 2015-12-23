package com.talkie.client.services.facebook

import com.facebook.login.LoginManager
import com.facebook.{ CallbackManager, Profile }
import com.talkie.client.services.facebook.FacebookMessages._
import com.talkie.client.services.{ LoggerComponent, Service, SyncService }

trait FacebookServices {

  def checkIfLogged: SyncService[CheckLoggedStatusRequest, CheckLoggedStatusResponse]
  def configureLogin: SyncService[ConfigureLoginRequest, ConfigureLoginResponse]
  def logout: SyncService[LogoutRequest, LogoutResponse]
  def processActivityResult: SyncService[ProcessActivityResultRequest, ProcessActivityResultResponse]
}

trait FacebookServicesComponent {

  def facebookServices: FacebookServices
}

trait FacebookServicesComponentImpl extends FacebookServicesComponent {
  self: LoggerComponent =>

  object facebookServices extends FacebookServices {

    private val callBackManager = CallbackManager.Factory.create()
    private val loginManager = LoginManager.getInstance()
    private val permissions = List("user_friends") // TODO: change into sth more reasonable

    override val checkIfLogged = Service { request: CheckLoggedStatusRequest =>
      CheckLoggedStatusResponse(Option(Profile.getCurrentProfile).isDefined)
    }

    override val configureLogin = Service { request: ConfigureLoginRequest =>
      val result = for {
        loginButton <- request.loginButtonOpt
      } yield loginButton.setReadPermissions(permissions: _*)

      ConfigureLoginResponse(result.isDefined)
    }
    
    override val logout = Service { request: LogoutRequest =>
      loginManager.logOut()
      LogoutResponse()
    }

    override val processActivityResult = Service { request: ProcessActivityResultRequest =>
      ProcessActivityResultResponse(callBackManager.onActivityResult(request.requestCode, request.resultCode, request.data))
    }
  }
}