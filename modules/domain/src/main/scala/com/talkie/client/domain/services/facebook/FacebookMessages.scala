package com.talkie.client.domain.services.facebook

import android.content.Intent
import com.facebook.login.widget.LoginButton

object FacebookMessages {

  case class CheckLoggedStatusRequest()
  case class CheckLoggedStatusResponse(isLogged: Boolean)

  case class ConfigureLoginRequest(loginButton: LoginButton)
  case class ConfigureLoginResponse()

  case class LogoutRequest()
  case class LogoutResponse()

  case class ProcessActivityResultRequest(requestCode: Int, resultCode: Int, data: Intent)
  case class ProcessActivityResultResponse(handled: Boolean)
}
