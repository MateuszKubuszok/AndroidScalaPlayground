package com.talkie.client.navigation.auth

import android.app.Activity
import android.content.Intent
import com.talkie.client.navigation.NavigateActivities
import com.talkie.client.services.LoggerComponent

trait NavigateOnLogin {
  self: Activity
   with LoggerComponent
   with NavigateActivities =>

  private val Extra_ActivityAfterLogin = "activity_after_login"

  protected def startLoginActivityThenReturnTo(intent: Intent) = {
    logger trace s"Requested FB login then ${intent.getComponent.getClassName}"
    startActivity(loginActivity().putExtra(Extra_ActivityAfterLogin, intent))
  }
  protected def returnFromLoginActivity() = {
    logger trace s"FB login succeeded, now into ${getIntent.getComponent.getClassName}"
    startActivity(getIntent.getParcelableExtra[Intent](Extra_ActivityAfterLogin))
  }
}
