package com.talkie.client.core.scheduler

import android.app.job.JobInfo
import com.talkie.client.common.scheduler.JobId

import scalaz.{ :<:, Free }

sealed trait SchedulerService[R]

object SchedulerService {

  final case class ScheduleJob(jobInfo: JobInfo) extends SchedulerService[Option[JobId]]
  final case class CancelJob(jobId: JobId) extends SchedulerService[Unit]

  class Ops[S[_]](implicit s0: SchedulerService :<: S) {

    def scheduleJob(jobInfo: JobInfo): Free[S, Option[JobId]] =
      Free.liftF(s0.inj(ScheduleJob(jobInfo)))

    def cancelJob(jobId: JobId): Free[S, Unit] =
      Free.liftF(s0.inj(CancelJob(jobId)))
  }
}
