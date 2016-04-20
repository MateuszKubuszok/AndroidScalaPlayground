package com.talkie.client.domain.services.facebook

import com.facebook.login.{ LoginManager, LoginResult }
import com.facebook._
import com.talkie.client.core.context.{ Context, CoreContext }
import com.talkie.client.core.events.EventBus
import com.talkie.client.core.events.EventMessages.NotifyEventListenersRequest
import com.talkie.client.core.logging.LoggerImpl
import com.talkie.client.domain.events.FacebookEvents._
import com.talkie.client.domain.services.facebook.FacebookMessages._
import com.talkie.client.core.services._

trait FacebookServices {

  def checkIfLogged: AsyncService[CheckLoggedStatusRequest, CheckLoggedStatusResponse, Context]
  def configureLogin: AsyncService[ConfigureLoginRequest, ConfigureLoginResponse, Context]
  def logout: AsyncService[LogoutRequest, LogoutResponse, Context]
  def processActivityResult: SyncService[ProcessActivityResultRequest, ProcessActivityResultResponse, Context]
}

object FacebookServices {

  private lazy val logger = new LoggerImpl(this.getClass.getSimpleName)

  private[facebook] lazy val callBackManager = CallbackManager.Factory.create()
  private[facebook] lazy val loginManager = LoginManager.getInstance()
  private[facebook] val permissions = List("public_profile", "user_photos")

  def ensureInitialized(context: Context): Unit = {
    if (!FacebookSdk.isInitialized) {
      require(context.androidContext.getApplicationContext != null, "ApplicationContext must be available")
      FacebookSdk.sdkInitialize(context.androidContext.getApplicationContext)
      logger info "Facebook SDK initialized"
    }
  }
}

class FacebookServicesImpl(context: Context with CoreContext, eventBus: EventBus) extends FacebookServices {

  FacebookServices.ensureInitialized(context)

  private val logger = context.loggerFor(this)

  private val permissions = List("public_profile", "user_photos")

  private object callback extends FacebookCallback[LoginResult] {

    private implicit val c = context

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

    val loginButton = request.loginButton
    loginButton.setReadPermissions(permissions: _*)
    loginButton.registerCallback(FacebookServices.callBackManager, callback)

    ConfigureLoginResponse()
  }

  override val logout = Service.async { request: LogoutRequest =>
    implicit val c = context

    logger trace "Requested logout"

    FacebookServices.loginManager.logOut()
    eventBus.notifyEventListeners(NotifyEventListenersRequest(LoggedOut()))

    LogoutResponse()
  }

  override val processActivityResult = Service { request: ProcessActivityResultRequest =>
    logger trace "Requested ActivityResult processing (should result in login)"
    val result = FacebookServices.callBackManager.onActivityResult(request.requestCode, request.resultCode, request.data)
    ProcessActivityResultResponse(result)
  }
}

