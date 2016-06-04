package com.talkie.client.core.scheduler

import com.talkie.client.common.scheduler.Job
import com.talkie.client.common.services.{ Service => GenericService }

import scala.concurrent.duration.Duration
import scalaz.Free

sealed trait SchedulerService[R] extends GenericService[R]
final case class ScheduleSingleJob(job: Job, delay: Duration) extends SchedulerService[Boolean]
final case class ScheduleIntervalJob(job: Job, initialDelay: Duration, delay: Duration)
  extends SchedulerService[Boolean]
final case class SchedulePeriodicJob(job: Job, initialDelay: Duration, period: Duration)
  extends SchedulerService[Boolean]
final case class CancelJob(job: Job, canInterrupt: Boolean) extends SchedulerService[Boolean]

trait SchedulerServiceFrees[S[R] >: SchedulerService[R]] {

  def scheduleSingleJob(job: Job, delay: Duration): Free[S, Boolean] =
    Free.liftF(ScheduleSingleJob(job, delay): S[Boolean])

  def scheduleIntervalJob(job: Job, initialDelay: Duration, delay: Duration): Free[S, Boolean] =
    Free.liftF(ScheduleIntervalJob(job, initialDelay, delay): S[Boolean])

  def schedulePeriodicJob(job: Job, initialDelay: Duration, period: Duration): Free[S, Boolean] =
    Free.liftF(SchedulePeriodicJob(job, initialDelay, period): S[Boolean])

  def cancelJob(job: Job, canInterrupt: Boolean): Free[S, Boolean] =
    Free.liftF(CancelJob(job, canInterrupt): S[Boolean])
}

object SchedulerService extends SchedulerServiceFrees[SchedulerService]
object Service extends SchedulerServiceFrees[GenericService]
