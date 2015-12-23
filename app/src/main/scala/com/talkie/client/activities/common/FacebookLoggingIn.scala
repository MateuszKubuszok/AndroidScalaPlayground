package com.talkie.client.activities.common

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.talkie.client.navigation.auth.NavigateOnLogin
import com.talkie.client.services.ContextComponent
import com.talkie.client.services.loginSetup.LoginSetupMessages.{ProcessActivityResultRequest, ConfigureLoginRequest}
import com.talkie.client.services.loginSetup.LoginSetupServicesComponent
import com.talkie.client.views.ActivityViews

trait FacebookLoggingIn extends Activity {
  self: ActivityViews
    with ContextComponent
    with LoginSetupServicesComponent
    with NavigateOnLogin =>

  implicit val c = context

  override protected def onPostCreate(savedInstanceState: Bundle) {
    super.onPostCreate(savedInstanceState)
    loginSetupServices.configureLogin(ConfigureLoginRequest(loginButtonOpt))
  }

  override protected def onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
    super.onActivityResult(requestCode, resultCode, data)
    loginSetupServices.processActivityResult(ProcessActivityResultRequest(requestCode, resultCode, data))
  }
}
