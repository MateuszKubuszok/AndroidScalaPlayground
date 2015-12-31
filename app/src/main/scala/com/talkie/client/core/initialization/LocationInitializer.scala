package com.talkie.client.core.initialization

import com.talkie.client.core.scheduler.SchedulerComponent
import com.talkie.client.domain.jobs.LocationJobsComponent

private[initialization] trait LocationInitializer extends Initialization {
  self: SchedulerComponent
    with LocationJobsComponent =>

  override def initialize() {
    super.initialize()
    scheduler.schedulePeriodicJob(locationJobs.turnOnLocationTrackingJob)
    scheduler.schedulePeriodicJob(locationJobs.turnOffLocationTrackingJob)
    scheduler.schedulePeriodicJob(locationJobs.checkLastLocationJob)
  }
}
