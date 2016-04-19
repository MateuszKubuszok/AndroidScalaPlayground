package com.talkie.client.core.scheduler

import java.util.concurrent.TimeUnit.MILLISECONDS

import com.talkie.client.core.scheduler.SchedulerMessages._
import com.talkie.client.core.services.{ Context, Service, SyncService }

trait Scheduler {

  def scheduleSingleJob: SyncService[ScheduleSingleJobRequest, ScheduleJobResponse]
  def scheduleIntervalJob: SyncService[ScheduleIntervalJobRequest, ScheduleJobResponse]
  def schedulePeriodicJob: SyncService[SchedulePeriodicJobRequest, ScheduleJobResponse]
  def cancelJob: SyncService[CancelJobRequest, CancelJobResponse]
}

class SchedulerImpl(context: Context) extends Scheduler {

  private val logger = context.loggerFor(this)
  private val executor = context.schedulerExecutor
  private val jobs = context.schedulerJobs

  override val scheduleSingleJob = Service { request: ScheduleSingleJobRequest =>
    val future = executor.schedule(CleanJobOnceFinished(request.job), request.delay.toMillis, MILLISECONDS)
    logger trace s"Single job scheduled: $request"
    ScheduleJobResponse(jobs.put(request.job, future).isEmpty)
  }

  override val scheduleIntervalJob = Service { request: ScheduleIntervalJobRequest =>
    val future = executor.scheduleWithFixedDelay(request.job, request.initialDelay.toMillis, request.delay.toMillis, MILLISECONDS)
    logger trace s"Interval job scheduled: $request"
    ScheduleJobResponse(jobs.put(request.job, future).isEmpty)
  }

  override val schedulePeriodicJob = Service { request: SchedulePeriodicJobRequest =>
    val future = executor.scheduleAtFixedRate(request.job, request.initialDelay.toMillis, request.period.toMillis, MILLISECONDS)
    logger trace s"Periodic job scheduled: $request"
    ScheduleJobResponse(jobs.put(request.job, future).isEmpty)
  }

  override val cancelJob = Service { request: CancelJobRequest =>
    val result = for {
      future <- jobs.get(request.job)
    } yield {
      future.cancel(request.canInterrupt)
      jobs.remove(request.job)
      logger trace s"Job cancelled: $request"
      future.isCancelled
    }
    CancelJobResponse(result getOrElse true)
  }

  case class CleanJobOnceFinished(job: Job) extends Job {

    override def run(): Unit = try {
      job.run()
    } finally {
      jobs.remove(job)
    }
  }
}
