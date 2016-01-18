package com.talkie.client.app.initialization

import com.talkie.client.app.navigation.AuthNavigationComponent
import com.talkie.client.core.events.EventBusComponent
import com.talkie.client.core.logging.LoggerComponent
import com.talkie.client.core.scheduler.SchedulerComponent
import com.talkie.client.core.services.ContextComponent
import com.talkie.client.domain.jobs.{ LocationJobsComponent, LocationJobsComponentImpl }
import com.talkie.client.domain.services.location.LocationServicesComponent
import com.talkie.client.views.common.RichActivity

trait AppInitialization
    extends RichActivity
    with Initialization
    with JodaTimeInitializer
    with FacebookInitializer
    with LocationInitializer
    with LocationJobsComponentImpl
    with AuthNavigationInitializer {
  self: ContextComponent with EventBusComponent with LoggerComponent with SchedulerComponent with LocationJobsComponent with LocationServicesComponent with AuthNavigationComponent =>

  onCreate {
    initializeApp()
  }
}
