package com.talkie.client.core.scheduler

import com.talkie.client.common.context.Context
import com.talkie.client.common.scheduler.Job
import com.talkie.client.common.services.{ ~@~>, ~&~> }

import scala.concurrent.duration.Duration
import scalaz.concurrent.Task

trait SchedulerServiceInterpreter extends (SchedulerService ~@~> Task)

object SchedulerServiceInterpreter extends (SchedulerService ~&~> Task)

final class SchedulerServiceInterpreterImpl(context: Context) extends SchedulerServiceInterpreter {

  private val actions = new SchedulerActions(context)

  override def apply[R](in: SchedulerService[R]): Task[R] = in match {

    case ScheduleSingleJob(job: Job, delay: Duration) => Task {
      actions.scheduleSingleJob(job, delay).asInstanceOf[R]
    }

    case ScheduleIntervalJob(job: Job, initialDelay: Duration, delay: Duration) => Task {
      actions.scheduleIntervalJob(job, initialDelay, delay).asInstanceOf[R]
    }

    case SchedulePeriodicJob(job: Job, initialDelay: Duration, period: Duration) => Task {
      actions.schedulePeriodicJob(job, initialDelay, period).asInstanceOf[R]
    }

    case CancelJob(job: Job, canInterrupt: Boolean) => Task {
      actions.cancelJob(job, canInterrupt).asInstanceOf[R]
    }
  }
}
