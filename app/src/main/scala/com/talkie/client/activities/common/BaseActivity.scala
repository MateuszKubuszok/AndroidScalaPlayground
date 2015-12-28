package com.talkie.client.activities.common

import android.app.Activity
import com.talkie.client.events.EventBusComponentImpl
import com.talkie.client.navigation.{AutomatedAuthNavigation, ManualNavigation}
import com.talkie.client.services.location.LocationServicesComponentImpl
import com.talkie.client.services.{ ContextComponentImpl, LoggerComponentImpl }
import com.talkie.client.services.facebook.FacebookServicesComponentImpl
import com.talkie.client.views.{ActivityViews, Listeners}

trait BaseActivity
    extends Activity
    with ActivityViews
    with AutomatedAuthNavigation
    with ContextComponentImpl
    with EventBusComponentImpl
    with FacebookServicesComponentImpl
    with LocationServicesComponentImpl
    with Listeners
    with LoggerComponentImpl
    with ManualNavigation
