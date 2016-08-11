package com.talkie.client.core.scheduler

import com.talkie.client.common.scheduler.JobId

import scalaz.~>
import scalaz.concurrent.Task

final class SchedulerServiceTaskInterpreter(implicit schedulerActions: SchedulerActions)
    extends (SchedulerService ~> Task) {

  import SchedulerService._

  override def apply[R](in: SchedulerService[R]): Task[R] = in match {

    case ScheduleJob(jobInfo) => Task {
      schedulerActions.scheduleJob(jobInfo).asInstanceOf[R]
    }

    case CancelJob(jobId: JobId) => Task {
      schedulerActions.cancelJob(jobId).asInstanceOf[R]
    }
  }
}
