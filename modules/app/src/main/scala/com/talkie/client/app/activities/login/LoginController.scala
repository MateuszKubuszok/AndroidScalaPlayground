package com.talkie.client.app.activities.login

import android.app.Activity
import android.content.Intent
import com.talkie.client.R
import com.talkie.client.app.activities.common.Controller
import com.talkie.client.domain.services.facebook.{ FacebookServicesComponent, FacebookMessages }
import FacebookMessages.{ ConfigureLoginRequest, ProcessActivityResultRequest }

trait LoginController extends Controller {
  self: Activity with FacebookServicesComponent =>

  implicit val c = context
  implicit val ec = context.executionContext

  final protected def onCreateEvent() {
    setContentView(R.layout.activity_login)
  }

  final protected def onPostCreateEvent() = asyncAction {
    facebookServices.configureLogin(ConfigureLoginRequest(loginButtonOpt)) map { result =>
      if (result.success) logger trace "LoginButton configured successfully"
      else logger assertionFailed "LoginButton couldn't be configured correctly"
    }
  }

  final protected def onActivityResultEvent(requestCode: Int, resultCode: Int, data: Intent) = asyncAction {
    val result = facebookServices.processActivityResult(ProcessActivityResultRequest(requestCode, resultCode, data))
    if (result.handled) logger trace "ActivityResult handled successfully"
    else logger error "ActivityResult not handled"
  }
}
