package com.talkie.client.domain.tracking

import scalaz.{ :<:, Free }

sealed trait TrackingService[R]

object TrackingService {

  case object StartTrackingJobs extends TrackingService[Unit]
  case object StopTrackingJobs extends TrackingService[Unit]

  class Ops[S[_]](implicit s0: TrackingService :<: S) {

    def startTrackingJobs: Free[S, Unit] =
      Free.liftF(s0.inj(StartTrackingJobs))

    def stopTrackingJobs: Free[S, Unit] =
      Free.liftF(s0.inj(StopTrackingJobs))
  }
}
