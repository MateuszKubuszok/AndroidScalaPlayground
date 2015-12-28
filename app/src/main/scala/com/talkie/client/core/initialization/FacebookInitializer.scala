package com.talkie.client.core.initialization

import android.app.Activity
import android.os.Bundle
import com.facebook.FacebookSdk

trait FacebookInitializer extends Activity {

  override protected def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    FacebookSdk.sdkInitialize(getApplicationContext)
  }
}
