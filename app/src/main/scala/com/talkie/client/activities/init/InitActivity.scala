package com.talkie.client.activities.init

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.talkie.client.R
import com.talkie.client.activities.common.BaseActivity
import com.talkie.client.initialization.FacebookInitializer

class InitActivity
    extends AppCompatActivity
    with BaseActivity
    with FacebookInitializer {

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
}