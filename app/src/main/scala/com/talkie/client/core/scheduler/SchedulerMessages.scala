package com.talkie.client.core.scheduler

import com.talkie.client.core.scheduler.JobType.JobType

import scala.concurrent.duration.Duration

object SchedulerMessages {

  case class ScheduleSingleJobRequest(job: Job, jobType: JobType, delay: Duration)
  case class ScheduleIntervalJobRequest(job: Job, jobType: JobType, initialDelay: Duration, delay: Duration)
  case class SchedulePeriodicJobRequest(job: Job, jobType: JobType, initialDelay: Duration, period: Duration)
  case class ScheduleJobResponse(success: Boolean)

  case class CancelJobRequest(job: Job, canInterrupt: Boolean)
  case class CancelJobResponse(success: Boolean)
}
