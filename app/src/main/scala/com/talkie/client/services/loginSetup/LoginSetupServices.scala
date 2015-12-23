package com.talkie.client.services.loginSetup

import com.facebook.login.LoginResult
import com.facebook.{CallbackManager, FacebookCallback, FacebookException}
import com.talkie.client.navigation.auth.NavigateOnLogin
import com.talkie.client.services.{LoggerComponent, Service, SyncService}
import com.talkie.client.services.loginSetup.LoginSetupMessages._

trait LoginSetupServices {

  def configureLogin: SyncService[ConfigureLoginRequest, ConfigureLoginResponse]
  def processActivityResult: SyncService[ProcessActivityResultRequest, ProcessActivityResultResponse]
}

trait LoginSetupServicesComponent {

  def loginSetupServices: LoginSetupServices
}

trait LoginSetupServicesComponentImpl extends LoginSetupServicesComponent {
  self: LoggerComponent
   with NavigateOnLogin =>

  object loginSetupServices extends LoginSetupServices {

    private val CallBackManager = CallbackManager.Factory.create()
    private val Permissions = List("user_friends") // TODO: change into sth more reasonable

    override val configureLogin: SyncService[ConfigureLoginRequest, ConfigureLoginResponse] =
      Service { (request, context) =>

        val result = for {
          loginButton <- request.loginButtonOpt
        } yield {
          loginButton.setReadPermissions(Permissions: _*)
          loginButton.registerCallback(CallBackManager, new FacebookCallback[LoginResult] {

            override def onSuccess(result: LoginResult) {
              logger trace s"FB login result: $result"
              returnFromLoginActivity()
            }

            override def onCancel() {
              logger warn "User canceled login"
            }

            override def onError(error: FacebookException) {
              logger warn "Login error"
            }
          })
        }

        ConfigureLoginResponse(result.isDefined)
      }

    override val processActivityResult: SyncService[ProcessActivityResultRequest, ProcessActivityResultResponse] =
      Service { (request, context) =>

        ProcessActivityResultResponse(CallBackManager.onActivityResult(request.requestCode, request.resultCode, request.data))
      }
  }
}