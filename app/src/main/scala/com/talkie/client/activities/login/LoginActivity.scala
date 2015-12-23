package com.talkie.client.activities.login

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.talkie.client.R
import com.talkie.client.activities.common.BaseActivity

class LoginActivity
    extends AppCompatActivity
    with BaseActivity
    with Login {

  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_login)
  }
}
