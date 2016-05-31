package com.talkie.client.core.location

import android.location.{ LocationListener => Listener }
import com.talkie.client.core.services.Service
import com.talkie.client.core.location.LocationProviders.{ LocationProvider => Provider }

import scalaz.Free

sealed trait LocationService[R] extends Service[R]
final case class CheckLastKnownLocation(providers: Provider*) extends LocationService[Boolean]
final case class RegisterLocationListener(listener: Listener, providers: Provider*) extends LocationService[Boolean]
final case class RemoveLocationListener(listener: Listener) extends LocationService[Boolean]

object LocationService {

  def checkLastKnownLocation(providers: Provider*): Free[LocationService, Boolean] =
    Free.liftF(CheckLastKnownLocation(providers:_*): LocationService[Boolean])

  def registerLocationListener(listener: Listener, providers: Provider*): Free[LocationService, Boolean] =
    Free.liftF(RegisterLocationListener(listener, providers:_*): LocationService[Boolean])

  def removeLocationListener(listener: Listener): Free[LocationService, Boolean] =
    Free.liftF(RemoveLocationListener(listener): LocationService[Boolean])
}
