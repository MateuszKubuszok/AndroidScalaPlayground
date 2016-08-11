package com.talkie.client.common.context

import com.talkie.client.common.scheduler.JobId

case class SharedState(
  scheduledJobs: Set[JobId] = Set()
)
