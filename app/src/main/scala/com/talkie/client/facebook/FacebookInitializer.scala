package com.talkie.client.facebook

import android.app.Activity
import android.os.Bundle
import com.facebook.FacebookSdk
import com.talkie.client.common.ActivityViews

trait FacebookInitializer extends Activity with ActivityViews {

  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    FacebookSdk.sdkInitialize(getApplicationContext)
  }
}
