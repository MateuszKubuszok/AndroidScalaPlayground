package com.talkie.client.domain.tracking

import scalaz.~>
import scalaz.concurrent.Task

final class TrackingServiceTaskInterpreter(
    implicit
    trackingActions: TrackingActions
) extends (TrackingService ~> Task) {

  import TrackingService._

  override def apply[R](in: TrackingService[R]): Task[R] = in match {

    case StartTrackingJobs => Task {
      trackingActions.startTrackingJobs().asInstanceOf[R]
    }

    case StopTrackingJobs => Task {
      trackingActions.stopTrackingJobs().asInstanceOf[R]
    }
  }
}
