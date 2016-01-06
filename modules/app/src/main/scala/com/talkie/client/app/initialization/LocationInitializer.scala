package com.talkie.client.app.initialization

import com.talkie.client.core.logging.LoggerComponent
import com.talkie.client.core.scheduler.SchedulerComponent
import com.talkie.client.core.services.ContextComponent
import com.talkie.client.domain.jobs.LocationJobsComponent

private[initialization] trait LocationInitializer extends Initialization {
  self: ContextComponent with LoggerComponent with SchedulerComponent with LocationJobsComponent =>

  onInitialization {
    implicit val c = context

    scheduler.schedulePeriodicJob(locationJobs.turnOnLocationTrackingJob)
    scheduler.schedulePeriodicJob(locationJobs.turnOffLocationTrackingJob)
    scheduler.schedulePeriodicJob(locationJobs.checkLastLocationJob)

    logger info "Location initialized"
  }
}
