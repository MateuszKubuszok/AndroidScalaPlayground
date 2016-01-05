package com.talkie.client.app.initialization

import com.talkie.client.core.scheduler.SchedulerComponent
import com.talkie.client.core.services.ContextComponent
import com.talkie.client.domain.jobs.LocationJobsComponent

private[initialization] trait LocationInitializer extends Initialization {
  self: ContextComponent with SchedulerComponent with LocationJobsComponent =>

  override def initialize() {
    super.initialize()

    implicit val c = context

    scheduler.schedulePeriodicJob(locationJobs.turnOnLocationTrackingJob)
    scheduler.schedulePeriodicJob(locationJobs.turnOffLocationTrackingJob)
    scheduler.schedulePeriodicJob(locationJobs.checkLastLocationJob)
  }
}