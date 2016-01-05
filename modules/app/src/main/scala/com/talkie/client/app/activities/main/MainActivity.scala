package com.talkie.client.app.activities.main

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.talkie.client.app.activities.common.BaseActivity
import com.talkie.client.app.initialization.AppInitialization

class MainActivity
    extends AppCompatActivity
    with BaseActivity
    with MainController
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
