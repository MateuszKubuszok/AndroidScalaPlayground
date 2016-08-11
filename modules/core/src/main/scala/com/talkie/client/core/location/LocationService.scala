package com.talkie.client.core.location

import android.location.{ LocationListener => Listener }
import com.talkie.client.core.location.LocationProviders.{ LocationProvider => Provider }

import scalaz.{ :<:, Free }

sealed trait LocationService[R]

object LocationService {

  final case class CheckLastKnownLocation() extends LocationService[Boolean]
  final case class RegisterLocationListener(listener: Listener, providers: Provider*) extends LocationService[Boolean]
  final case class RemoveLocationListener(listener: Listener) extends LocationService[Boolean]

  class Ops[S[_]](implicit s0: LocationService :<: S) {

    def checkLastKnownLocation(): Free[S, Boolean] =
      Free.liftF(s0.inj(CheckLastKnownLocation()))

    def registerLocationListener(listener: Listener, providers: Provider*): Free[S, Boolean] =
      Free.liftF(s0.inj(RegisterLocationListener(listener, providers: _*)))

    def removeLocationListener(listener: Listener): Free[S, Boolean] =
      Free.liftF(s0.inj(RemoveLocationListener(listener)))
  }
}
