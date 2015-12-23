package com.talkie.client.services.loginSetup

import android.content.Intent
import com.facebook.login.widget.LoginButton

object LoginSetupMessages {

  case class ConfigureLoginRequest(loginButtonOpt: Option[LoginButton])
  case class ConfigureLoginResponse(success: Boolean)

  case class ProcessActivityResultRequest(requestCode: Int, resultCode: Int, data: Intent)
  case class ProcessActivityResultResponse(handled: Boolean)
}
