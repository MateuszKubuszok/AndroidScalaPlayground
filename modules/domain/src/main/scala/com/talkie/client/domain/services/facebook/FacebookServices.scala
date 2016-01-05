package com.talkie.client.domain.services.facebook

import com.facebook.login.{ LoginResult, LoginManager }
import com.facebook.{ FacebookException, FacebookCallback, CallbackManager, Profile }
import com.talkie.client.core.events.EventBusComponent
import com.talkie.client.core.events.EventMessages.NotifyEventListenersRequest
import com.talkie.client.core.logging.LoggerComponent
import com.talkie.client.domain.events.FacebookEvents._
import com.talkie.client.domain.services.facebook.FacebookMessages._
import com.talkie.client.core.services._

trait FacebookServices {

  def checkIfLogged: AsyncService[CheckLoggedStatusRequest, CheckLoggedStatusResponse]
  def configureLogin: AsyncService[ConfigureLoginRequest, ConfigureLoginResponse]
  def logout: AsyncService[LogoutRequest, LogoutResponse]
  def processActivityResult: SyncService[ProcessActivityResultRequest, ProcessActivityResultResponse]
}

trait FacebookServicesComponent {

  def facebookServices: FacebookServices
}

trait FacebookServicesComponentImpl extends FacebookServicesComponent {
  self: ContextComponent with EventBusComponent with LoggerComponent =>

  object facebookServices extends FacebookServices {

    private lazy val callBackManager = CallbackManager.Factory.create()
    private lazy val loginManager = LoginManager.getInstance()
    private val permissions = List("public_profile", "user_photos")

    private object callback extends FacebookCallback[LoginResult] {

      implicit val c = context

      override def onSuccess(result: LoginResult) = {
        logger info s"Logged in: $result"
        eventBus.notifyEventListeners(NotifyEventListenersRequest(LoginSucceeded(result)))
      }

      override def onCancel() = {
        logger info "Login cancelled"
        eventBus.notifyEventListeners(NotifyEventListenersRequest(LoginCancelled()))
      }

      override def onError(error: FacebookException) = {
        logger error ("Login failed", error)
        eventBus.notifyEventListeners(NotifyEventListenersRequest(LoginFailed(error)))
      }
    }

    override val checkIfLogged = Service.async { request: CheckLoggedStatusRequest =>
      logger trace "Requested login status check"
      CheckLoggedStatusResponse(Option(Profile.getCurrentProfile).isDefined)
    }

    override val configureLogin = Service.async { request: ConfigureLoginRequest =>
      logger trace "Requested loginButton configuration"
      val result = for {
        loginButton <- request.loginButtonOpt
      } yield {
        loginButton.setReadPermissions(permissions: _*)
        loginButton.registerCallback(callBackManager, callback)
      }

      ConfigureLoginResponse(result.isDefined)
    }

    override val logout = Service.async { request: LogoutRequest =>
      logger trace "Requested logout"
      loginManager.logOut()
      LogoutResponse()
    }

    override val processActivityResult = Service { request: ProcessActivityResultRequest =>
      logger trace "Requested ActivityResult processing (should result in login)"
      val result = callBackManager.onActivityResult(request.requestCode, request.resultCode, request.data)
      ProcessActivityResultResponse(result)
    }
  }
}
