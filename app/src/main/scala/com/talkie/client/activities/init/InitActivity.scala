package com.talkie.client.activities.init

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.talkie.client.activities.common.BaseActivity
import com.talkie.client.initialization.AppInitialization

class InitActivity
    extends AppCompatActivity
    with BaseActivity
    with InitController
    with AppInitialization {

  override protected def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    onCreateEvent()
  }

  override protected def onStart() {
    super.onStart()
    onStartEvent()
  }

  override protected def onRestart() {
    super.onRestart()
    onRestartEvent()
  }
}