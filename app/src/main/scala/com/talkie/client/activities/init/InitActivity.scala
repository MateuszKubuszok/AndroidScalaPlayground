package com.talkie.client.activities.init

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.talkie.client.activities.common.BaseActivity
import com.talkie.client.core.initialization.AppInitialization

class InitActivity
    extends AppCompatActivity
    with BaseActivity
    with InitController
    with AppInitialization {

  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    onCreateEvent()
  }

  override def onStart() {
    super.onStart()
    onStartEvent()
  }

  override def onRestart() {
    super.onRestart()
    onRestartEvent()
  }
}