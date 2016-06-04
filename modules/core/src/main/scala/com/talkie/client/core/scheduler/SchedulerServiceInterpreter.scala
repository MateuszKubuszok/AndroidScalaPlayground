package com.talkie.client.core.scheduler

import java.util.concurrent.TimeUnit.MILLISECONDS

import com.talkie.client.common.context.Context
import com.talkie.client.common.scheduler.Job
import com.talkie.client.common.context.SharedState
import com.talkie.client.common.services.{ ~@~>, ~&~> }

import scala.concurrent.duration.Duration
import scalaz.concurrent.Task

trait SchedulerServiceInterpreter extends (SchedulerService ~@~> Task)

object SchedulerServiceInterpreter extends (SchedulerService ~&~> Task)

final class SchedulerServiceInterpreterImpl(context: Context) extends SchedulerServiceInterpreter {

  private val logger = context.loggerFor(this)
  private val executor = context.schedulerExecutor

  override def apply[R](in: SchedulerService[R]): Task[R] = in match {

    case ScheduleSingleJob(job: Job, delay: Duration) => Task {
      scheduleSingleJob(job, delay).asInstanceOf[R]
    }

    case ScheduleIntervalJob(job: Job, initialDelay: Duration, delay: Duration) => Task {
      scheduleIntervalJob(job, initialDelay, delay).asInstanceOf[R]
    }

    case SchedulePeriodicJob(job: Job, initialDelay: Duration, period: Duration) => Task {
      schedulePeriodicJob(job, initialDelay, period).asInstanceOf[R]
    }

    case CancelJob(job: Job, canInterrupt: Boolean) => Task {
      cancelJob(job, canInterrupt).asInstanceOf[R]
    }
  }

  private def scheduleSingleJob(job: Job, delay: Duration): Boolean =
    context.withSharedStateUpdated { state =>
      val future = executor.schedule(CleanJobOnceFinished(job), delay.toMillis, MILLISECONDS) // TODO :(
      val newState = state.copy(scheduledJobs = state.scheduledJobs + (job -> future))
      val updated = state.scheduledJobs.size != newState.scheduledJobs.size
      logger trace s"Single job scheduled?: $job -> $updated"
      newState -> updated
    }

  private def scheduleIntervalJob(job: Job, initialDelay: Duration, delay: Duration): Boolean =
    context.withSharedStateUpdated { state =>
      val future = executor.scheduleWithFixedDelay(job, initialDelay.toMillis, delay.toMillis, MILLISECONDS) // TODO :(
      val newState = state.copy(scheduledJobs = state.scheduledJobs + (job -> future))
      val updated = state.scheduledJobs.size != newState.scheduledJobs.size
      logger trace s"Single job scheduled?: $job -> $updated"
      newState -> updated
    }

  private def schedulePeriodicJob(job: Job, initialDelay: Duration, period: Duration): Boolean =
    context.withSharedStateUpdated { state =>
      val future = executor.scheduleAtFixedRate(job, initialDelay.toMillis, period.toMillis, MILLISECONDS)
      val newState = state.copy(scheduledJobs = state.scheduledJobs + (job -> future))
      val updated = state.scheduledJobs.size != newState.scheduledJobs.size
      logger trace s"Single job scheduled?: $job -> $updated"
      newState -> updated
    }

  private def cancelJob(job: Job, canInterrupt: Boolean): Boolean =
    context.withSharedStateUpdated { state =>
      val result = for {
        future <- state.scheduledJobs.get(job)
      } yield {
        future.cancel(canInterrupt)
        if (future.isCancelled) state.copy(scheduledJobs = state.scheduledJobs - job)
        else state
      }
      val newState = result getOrElse state
      val updated = state.scheduledJobs.size != newState.scheduledJobs.size
      logger trace s"Job cancelled?: $job -> $updated"
      newState -> updated
    }

  case class CleanJobOnceFinished(job: Job) extends Job {

    override def run(): Unit = try {
      job.run()
    } finally {
      context.withSharedStateUpdated { state: SharedState =>
        (state.copy(scheduledJobs = state.scheduledJobs - job), ())
      }
    }
  }
}
