package com.talkie.client.activities.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.talkie.client.services.facebook.{FacebookServicesComponent, FacebookMessages}
import FacebookMessages.{ConfigureLoginRequest, ProcessActivityResultRequest}
import com.talkie.client.services.ContextComponent
import com.talkie.client.views.ActivityViews

trait Login extends Activity {
  self: ActivityViews
    with ContextComponent
    with FacebookServicesComponent =>

  implicit val c = context

  override protected def onPostCreate(savedInstanceState: Bundle) {
    super.onPostCreate(savedInstanceState)
    facebookServices.configureLogin(ConfigureLoginRequest(loginButtonOpt))
  }

  override protected def onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
    super.onActivityResult(requestCode, resultCode, data)
    facebookServices.processActivityResult(ProcessActivityResultRequest(requestCode, resultCode, data))
  }
}
