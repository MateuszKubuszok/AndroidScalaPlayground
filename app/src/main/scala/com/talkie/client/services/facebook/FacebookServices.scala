package com.talkie.client.services.facebook

import com.facebook.login.{LoginResult, LoginManager}
import com.facebook.{FacebookException, FacebookCallback, CallbackManager, Profile}
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

    private lazy val callBackManager = CallbackManager.Factory.create()
    private lazy val loginManager = LoginManager.getInstance()
    private val permissions = List("user_friends") // TODO: change into sth more reasonable

    override val checkIfLogged = Service { request: CheckLoggedStatusRequest =>
      logger trace "Requested login status check"
      CheckLoggedStatusResponse(Option(Profile.getCurrentProfile).isDefined)
    }

    override val configureLogin = Service { request: ConfigureLoginRequest =>
      logger trace "Requested loginButton configuration"
      val result = for {
        loginButton <- request.loginButtonOpt
      } yield loginButton.setReadPermissions(permissions: _*)

      ConfigureLoginResponse(result.isDefined)
    }
    
    override val logout = Service { request: LogoutRequest =>
      logger trace "Requested logout"
      loginManager.logOut()
      LogoutResponse()
    }

    override val processActivityResult = Service { request: ProcessActivityResultRequest =>
      logger trace "Requested ActivityResult processing (should result in login)"
      ProcessActivityResultResponse(callBackManager.onActivityResult(request.requestCode, request.resultCode, request.data))
    }
  }
}