package com.talkie.client.services.facebook

import com.facebook.login.LoginManager
import com.facebook.{CallbackManager, Profile}
import com.talkie.client.services.facebook.FacebookMessages._
import com.talkie.client.services._

trait FacebookServices {

  def checkIfLogged: AsyncService[CheckLoggedStatusRequest, CheckLoggedStatusResponse]
  def configureLogin: AsyncService[ConfigureLoginRequest, ConfigureLoginResponse]
  def logout: AsyncService[LogoutRequest, LogoutResponse]
  def processActivityResult: AsyncService[ProcessActivityResultRequest, ProcessActivityResultResponse]
}

trait FacebookServicesComponent {

  def facebookServices: FacebookServices
}

trait FacebookServicesComponentImpl extends FacebookServicesComponent {
  self: LoggerComponent =>

  object facebookServices extends FacebookServices {

    private lazy val callBackManager = CallbackManager.Factory.create()
    private lazy val loginManager = LoginManager.getInstance()
    private val permissions = List("user_profile", "user_photos")

    override val checkIfLogged = Service.async { request: CheckLoggedStatusRequest =>
      logger trace "Requested login status check"
      CheckLoggedStatusResponse(Option(Profile.getCurrentProfile).isDefined)
    }

    override val configureLogin = Service.async { request: ConfigureLoginRequest =>
      logger trace "Requested loginButton configuration"
      val result = for {
        loginButton <- request.loginButtonOpt
      } yield loginButton.setReadPermissions(permissions: _*)

      ConfigureLoginResponse(result.isDefined)
    }
    
    override val logout = Service.async { request: LogoutRequest =>
      logger trace "Requested logout"
      loginManager.logOut()
      LogoutResponse()
    }

    override val processActivityResult = Service.async { request: ProcessActivityResultRequest =>
      logger trace "Requested ActivityResult processing (should result in login)"
      ProcessActivityResultResponse(callBackManager.onActivityResult(request.requestCode, request.resultCode, request.data))
    }
  }
}