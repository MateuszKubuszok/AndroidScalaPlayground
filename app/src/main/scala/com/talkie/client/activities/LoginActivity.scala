package com.talkie.client.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.talkie.client.R
import com.talkie.client.activities.common.{BaseActivity, FacebookLoggingIn}
import com.talkie.client.navigation.auth.NavigateOnLogin
import com.talkie.client.services.ContextComponentImpl
import com.talkie.client.services.loginSetup.LoginSetupServicesComponentImpl

class LoginActivity
    extends AppCompatActivity
    with BaseActivity
    with ContextComponentImpl
    with FacebookLoggingIn
    with NavigateOnLogin
    with LoginSetupServicesComponentImpl {

  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_login)
  }
}
