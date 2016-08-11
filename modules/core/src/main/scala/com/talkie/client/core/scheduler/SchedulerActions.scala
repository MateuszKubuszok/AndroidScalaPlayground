package com.talkie.client.core.scheduler

import android.app.job.{ JobInfo, JobScheduler }
import android.content.Context._
import com.talkie.client.common.context.Context
import com.talkie.client.common.scheduler.JobId

trait SchedulerActions {

  def scheduleJob(jobInfo: JobInfo): Option[JobId]

  def cancelJob(jobId: JobId): Unit
}

final class SchedulerActionsImpl(implicit context: Context) extends SchedulerActions {

  private val logger = context.loggerFor(this)

  private lazy val scheduler = context.androidContext.getSystemService(JOB_SCHEDULER_SERVICE).asInstanceOf[JobScheduler]

  def scheduleJob(jobInfo: JobInfo): Option[JobId] =
    context.withSharedStateUpdated { state =>
      val (newState, updated) = scheduler.schedule(jobInfo) match {
        case jobId if jobId > 0 =>
          val job = JobId(jobId)
          state.copy(scheduledJobs = state.scheduledJobs + job) -> Some(job)
        case _ =>
          state -> None
      }
      logger trace s"Job scheduled?: $jobInfo -> $updated"
      newState -> updated
    }

  def cancelJob(jobId: JobId): Unit =
    context.withSharedStateUpdated { state =>
      scheduler.cancel(jobId.jobId)
      val newState = state.copy(scheduledJobs = state.scheduledJobs - jobId)
      val updated = state.scheduledJobs.size != newState.scheduledJobs.size
      logger trace s"Job cancelled?: $jobId -> $updated"
      newState -> updated
    }
}
