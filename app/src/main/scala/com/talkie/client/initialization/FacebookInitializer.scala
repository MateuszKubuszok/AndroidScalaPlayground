package com.talkie.client.initialization

import android.app.Activity
import android.os.Bundle
import com.facebook.FacebookSdk

trait FacebookInitializer extends Activity {

  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    FacebookSdk.sdkInitialize(getApplicationContext)
  }
}
