package com.talkie.client.app.activities.common

import android.app.Activity
import com.talkie.client.core.events.EventBusComponentImpl
import com.talkie.client.core.logging.LoggerComponentImpl
import com.talkie.client.core.permissions.PermissionsServicesComponentImpl
import com.talkie.client.core.scheduler.SchedulerComponentImpl
import com.talkie.client.app.navigation.{ AutomatedAuthNavigation, ManualNavigation }
import com.talkie.client.domain.services.location.LocationServicesComponentImpl
import com.talkie.client.core.services.ContextComponentImpl
import com.talkie.client.domain.services.facebook.FacebookServicesComponentImpl
import com.talkie.client.app.views.{ ActivityViews, Listeners }

trait BaseActivity
  extends Activity
  // core
  with ContextComponentImpl
  with EventBusComponentImpl
  with LoggerComponentImpl
  with PermissionsServicesComponentImpl
  with SchedulerComponentImpl
  // services
  with FacebookServicesComponentImpl
  with LocationServicesComponentImpl
  // navigation
  with AutomatedAuthNavigation
  with ManualNavigation
  // views
  with ActivityViews
  with Listeners
