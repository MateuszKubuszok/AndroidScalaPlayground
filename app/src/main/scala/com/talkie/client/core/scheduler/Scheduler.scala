package com.talkie.client.core.scheduler

import java.util.concurrent.TimeUnit.MILLISECONDS
import java.util.concurrent.{ScheduledFuture, Executors}

import com.talkie.client.core.logging.LoggerComponent
import com.talkie.client.core.scheduler.SchedulerMessages._
import com.talkie.client.core.services.{Service, SyncService}

import scala.collection.mutable

trait Scheduler {

  def scheduleSingleJob: SyncService[ScheduleSingleJobRequest, ScheduleJobResponse]
  def scheduleIntervalJob: SyncService[ScheduleIntervalJobRequest, ScheduleJobResponse]
  def schedulePeriodicJob: SyncService[SchedulePeriodicJobRequest, ScheduleJobResponse]
  def cancelJob: SyncService[CancelJobRequest, CancelJobResponse]
}

trait SchedulerComponent {

  def scheduler: Scheduler
}

private[scheduler] object SchedulerComponentImpl {

  private val executor = Executors.newScheduledThreadPool(2)

  private val jobs = mutable.Map[Job, ScheduledFuture[_]]()
}

trait SchedulerComponentImpl extends SchedulerComponent {
  self: LoggerComponent =>

  private def getExecutor = {
    import SchedulerComponentImpl.executor
    executor
  }

  private def getJobs = {
    import SchedulerComponentImpl.jobs
    jobs
  }

  object scheduler extends Scheduler {

    override val scheduleSingleJob = Service { request: ScheduleSingleJobRequest =>
      val future = getExecutor.schedule(CleanJobOnceFinished(request.job), request.delay.toMillis, MILLISECONDS)
      logger trace s"Single job scheduled: $request"
      ScheduleJobResponse(getJobs.put(request.job, future).isEmpty)
    }
    
    override val scheduleIntervalJob = Service { request: ScheduleIntervalJobRequest =>
      val future = getExecutor.scheduleWithFixedDelay(request.job, request.initialDelay.toMillis, request.delay.toMillis, MILLISECONDS)
      logger trace s"Interval job scheduled: $request"
      ScheduleJobResponse(getJobs.put(request.job, future).isEmpty)
    }
    
    override val schedulePeriodicJob = Service { request: SchedulePeriodicJobRequest =>
      val future = getExecutor.scheduleAtFixedRate(request.job, request.initialDelay.toMillis, request.period.toMillis, MILLISECONDS)
      logger trace s"Periodic job scheduled: $request"
      ScheduleJobResponse(getJobs.put(request.job, future).isEmpty)
    }
    
    override val cancelJob = Service { request: CancelJobRequest =>
      val result = for {
        future <- getJobs.get(request.job)
      } yield {
          future.cancel(request.canInterrupt)
          getJobs.remove(request.job)
          logger trace s"Job cancelled: $request"
          future.isCancelled
        }
      CancelJobResponse(result getOrElse true)
    }
    
    case class CleanJobOnceFinished(job: Job) extends Job {
      
      override def run(): Unit = try {
        job.run()
      } finally {
        getJobs.remove(job)
      }
    }
  }
}