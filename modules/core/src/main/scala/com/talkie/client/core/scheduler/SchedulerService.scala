package com.talkie.client.core.scheduler

import com.talkie.client.core.services.Service

import scala.concurrent.duration.Duration
import scalaz.Free

sealed trait SchedulerService[R] extends Service[R]
final case class ScheduleSingleJob(job: Job, delay: Duration) extends SchedulerService[Boolean]
final case class ScheduleIntervalJob(job: Job, initialDelay: Duration, delay: Duration)
  extends SchedulerService[Boolean]
final case class SchedulePeriodicJob(job: Job, initialDelay: Duration, period: Duration)
  extends SchedulerService[Boolean]
final case class CancelJob(job: Job, canInterrupt: Boolean) extends SchedulerService[Boolean]

object SchedulerService {

  def scheduleSingleJob(job: Job, delay: Duration): Free[SchedulerService, Boolean] =
    Free.liftF(ScheduleSingleJob(job, delay): SchedulerService[Boolean])

  def scheduleIntervalJob(job: Job, initialDelay: Duration, delay: Duration): Free[SchedulerService, Boolean] =
    Free.liftF(ScheduleIntervalJob(job, initialDelay, delay): SchedulerService[Boolean])

  def schedulePeriodicJob(job: Job, initialDelay: Duration, period: Duration): Free[SchedulerService, Boolean] =
    Free.liftF(SchedulePeriodicJob(job, initialDelay, period): SchedulerService[Boolean])

  def cancelJob(job: Job, canInterrupt: Boolean): Free[SchedulerService, Boolean] =
    Free.liftF(CancelJob(job, canInterrupt): SchedulerService[Boolean])
}
