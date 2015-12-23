package com.talkie.client.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.talkie.client.R
import com.talkie.client.activities.common.BaseActivity
import com.talkie.client.domain.facebook.FacebookMessages.CheckLoggedStatusRequest
import com.talkie.client.domain.facebook.FacebookServicesComponentImpl
import com.talkie.client.initialization.FacebookInitializer
import com.talkie.client.navigation.auth.NavigateOnLogin
import com.talkie.client.services.ContextComponentImpl

class InitActivity
    extends AppCompatActivity
    with BaseActivity
    with ContextComponentImpl
    with FacebookInitializer
    with FacebookServicesComponentImpl
    with NavigateOnLogin {

  implicit val c = context

  override protected def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_init)
    logger trace "Initial activity initialized"
  }

  override protected def onStart() {
    super.onStart()
    moveToMainIfLogged()
  }

  override protected def onRestart() {
    super.onRestart()
    moveToMainIfLogged()
  }

  private def moveToMainIfLogged() =
    if (facebookServices.checkIfLogged(CheckLoggedStatusRequest()).isLogged) {
      logger trace "Move on to MainActivity"
      startMainActivity()
    } else {
      logger trace "Move on to LoginActivity"
      startLoginActivityThenReturnTo(mainActivity())
    }
}