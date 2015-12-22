package com.talkie.client

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.talkie.client.facebook.{FacebookActivity, FacebookInitializer}

class InitActivity
    extends AppCompatActivity
    with FacebookActivity
    with FacebookInitializer {

  override protected def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_init)
    logger trace "Initial activity initialized"
  }

  override protected def onPostCreate(savedInstanceState: Bundle) {
    super.onPostCreate(savedInstanceState)
    startLoginActivityThenReturnTo(mainActivity())
  }
}