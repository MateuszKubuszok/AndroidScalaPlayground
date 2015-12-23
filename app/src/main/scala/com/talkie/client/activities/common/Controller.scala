package com.talkie.client.activities.common

import android.app.Activity
import com.talkie.client.navigation.ManualNavigation
import com.talkie.client.services.{ContextComponent, LoggerComponent}
import com.talkie.client.views.ActivityViews

trait Controller
    extends ActivityViews
    with ContextComponent
    with LoggerComponent
    with ManualNavigation {
  self: Activity =>
}
