package com.talkie.client.core.scheduler

import scala.concurrent.duration.Duration

object SchedulerMessages {

  case class ScheduleSingleJobRequest(job: Job, delay: Duration)
  case class ScheduleIntervalJobRequest(job: Job, initialDelay: Duration, delay: Duration)
  case class SchedulePeriodicJobRequest(job: Job, initialDelay: Duration, period: Duration)
  case class ScheduleJobResponse(success: Boolean)

  case class CancelJobRequest(job: Job, canInterrupt: Boolean)
  case class CancelJobResponse(success: Boolean)
}
