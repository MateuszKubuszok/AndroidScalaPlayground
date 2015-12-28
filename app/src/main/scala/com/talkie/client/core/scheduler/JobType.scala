package com.talkie.client.core.scheduler

object JobType extends Enumeration {
  type JobType = Value
  val NoRepeat, FixedDelay, FixedRate = Value
}
