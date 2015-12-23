package com.talkie.client.activities.login

import android.app.Activity
import android.content.Intent
import com.talkie.client.R
import com.talkie.client.activities.common.Controller
import com.talkie.client.services.facebook.{FacebookServicesComponent, FacebookMessages}
import FacebookMessages.{ConfigureLoginRequest, ProcessActivityResultRequest}

trait LoginController extends Controller {
  self: Activity
    with FacebookServicesComponent =>

  implicit val c = context

  protected def onCreateEvent() {
    setContentView(R.layout.activity_login)
  }

  protected def onPostCreateEvent() {
    facebookServices.configureLogin(ConfigureLoginRequest(loginButtonOpt))
  }

  protected def onActivityResultEvent(requestCode: Int, resultCode: Int, data: Intent) {
    facebookServices.processActivityResult(ProcessActivityResultRequest(requestCode, resultCode, data))
  }
}
