package com.talkie.client.activities.login

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.talkie.client.activities.common.BaseActivity

class LoginActivity
    extends AppCompatActivity
    with BaseActivity
    with LoginController {

  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    onCreateEvent()
  }

  override def onPostCreate(savedInstanceState: Bundle) {
    super.onPostCreate(savedInstanceState)
    onPostCreateEvent()
  }

  override def onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
    super.onActivityResult(requestCode, resultCode, data)
    onActivityResultEvent(requestCode, resultCode, data)
  }
}
