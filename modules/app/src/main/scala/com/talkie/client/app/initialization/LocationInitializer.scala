package com.talkie.client.app.initialization

import com.talkie.client.core.scheduler.Scheduler
import com.talkie.client.core.services.Context
import com.talkie.client.domain.jobs.LocationJobs

class LocationInitializer(
    context:      Context,
    scheduler:    Scheduler,
    locationJobs: LocationJobs
) extends Initialization {

  def initialize() {
    implicit val c = context

    scheduler.schedulePeriodicJob(locationJobs.turnOnLocationTrackingJob)
    scheduler.schedulePeriodicJob(locationJobs.turnOffLocationTrackingJob)
    scheduler.schedulePeriodicJob(locationJobs.checkLastLocationJob)

    context.loggerFor(this) info "Location initialized"
  }
}
