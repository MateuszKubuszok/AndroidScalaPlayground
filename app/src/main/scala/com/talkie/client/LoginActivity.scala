package com.talkie.client

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.talkie.client.facebook.FacebookLoggingIn

class LoginActivity extends AppCompatActivity with FacebookLoggingIn {

  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_login)
  }
}
