package com.talkie.client.domain.tracking

import android.app.job.JobInfo.Builder
import android.content.ComponentName

import com.talkie.client.common.context.Context
import com.talkie.client.common.scheduler.JobId
import com.talkie.client.core.scheduler.SchedulerActions

import scala.concurrent.duration._

trait TrackingActions {

  def startTrackingJobs(): Unit

  def stopTrackingJobs(): Unit
}

final class TrackingActionsImpl(
    implicit
    context:          Context,
    schedulerActions: SchedulerActions
) extends TrackingActions {

  private val logger = context.loggerFor(this)

  private val checkLastKnownLocationId = 12300066 // TODO: resources
  private val checkLastKnownLocationCN = new ComponentName(context.androidContext, classOf[CheckLastKnownLocationJob])
  private val checkLastKnownLocationInfo = new Builder(checkLastKnownLocationId, checkLastKnownLocationCN)
    .setPeriodic(1.minute.toMillis)
    .build()

  private val turnOnTrackingId = 12300067 // TODO: resources
  private val turnOnTrackingCN = new ComponentName(context.androidContext, classOf[TurnOnLocationTrackingJob])
  private val turnOnTrackingInfo = new Builder(turnOnTrackingId, turnOnTrackingCN)
    .setPeriodic(1.minute.toMillis)
    .build()

  private val turnOffTrackingId = 12300068 // TODO: resources
  private val turnOffTrackingCN = new ComponentName(context.androidContext, classOf[TurnOffLocationTrackingJob])
  private val turnOffTrackingInfo = new Builder(turnOffTrackingId, turnOffTrackingCN)
    .setPeriodic(2.minute.toMillis)
    .build()

  def startTrackingJobs(): Unit = {
    logger trace "Start tracking jobs"
    schedulerActions.scheduleJob(turnOnTrackingInfo)
    schedulerActions.scheduleJob(turnOffTrackingInfo)
    schedulerActions.scheduleJob(checkLastKnownLocationInfo)
  }

  def stopTrackingJobs(): Unit = {
    logger trace "Stop tracking jobs"
    schedulerActions.cancelJob(JobId(turnOnTrackingId))
    schedulerActions.cancelJob(JobId(turnOffTrackingId))
    schedulerActions.cancelJob(JobId(checkLastKnownLocationId))
  }
}
