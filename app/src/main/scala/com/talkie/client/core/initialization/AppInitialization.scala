package com.talkie.client.core.initialization

import android.app.Activity
import android.os.Bundle
import com.talkie.client.core.events.EventBusComponent
import com.talkie.client.core.logging.LoggerComponent
import com.talkie.client.core.scheduler.SchedulerComponent
import com.talkie.client.core.services.ContextComponent
import com.talkie.client.domain.jobs.{LocationJobsComponent, LocationJobsComponentImpl}
import com.talkie.client.domain.services.location.LocationServicesComponent

trait AppInitialization
    extends Activity
    with Initialization
    with FacebookInitializer
    with LocationInitializer
    with LocationJobsComponentImpl {
  self: ContextComponent
    with EventBusComponent
    with LoggerComponent
    with SchedulerComponent
    with LocationJobsComponent
    with LocationServicesComponent =>

  override protected def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    initialize()
  }
}
