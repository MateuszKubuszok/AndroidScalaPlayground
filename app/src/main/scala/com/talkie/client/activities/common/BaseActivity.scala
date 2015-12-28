package com.talkie.client.activities.common

import android.app.Activity
import com.talkie.client.core.events.EventBusComponentImpl
import com.talkie.client.core.logging.LoggerComponentImpl
import com.talkie.client.core.scheduler.SchedulerComponentImpl
import com.talkie.client.navigation.{AutomatedAuthNavigation, ManualNavigation}
import com.talkie.client.domain.services.location.LocationServicesComponentImpl
import com.talkie.client.core.services.ContextComponentImpl
import com.talkie.client.domain.services.facebook.FacebookServicesComponentImpl
import com.talkie.client.views.{ActivityViews, Listeners}

trait BaseActivity
    extends Activity
    with ActivityViews
    // core
    with ContextComponentImpl
    with EventBusComponentImpl
    with LoggerComponentImpl
    with SchedulerComponentImpl
    // services
    with FacebookServicesComponentImpl
    with LocationServicesComponentImpl
    // navigation
    with AutomatedAuthNavigation
    with ManualNavigation
    // utils
    with Listeners
