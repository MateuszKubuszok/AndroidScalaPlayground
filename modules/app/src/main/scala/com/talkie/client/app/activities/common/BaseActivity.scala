package com.talkie.client.app.activities.common

import com.talkie.client.core.events.EventBusComponentImpl
import com.talkie.client.core.logging.LoggerComponentImpl
import com.talkie.client.core.permissions.PermissionsServicesComponentImpl
import com.talkie.client.core.scheduler.SchedulerComponentImpl
import com.talkie.client.app.navigation.{ AuthNavigationComponentImpl, AccessTokenObserver, ManualNavigation }
import com.talkie.client.domain.services.location.LocationServicesComponentImpl
import com.talkie.client.core.services.ContextComponentImpl
import com.talkie.client.domain.services.facebook.FacebookServicesComponentImpl

trait BaseActivity
  extends RichActivity
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
  with AccessTokenObserver
  with AuthNavigationComponentImpl
  with ManualNavigation
