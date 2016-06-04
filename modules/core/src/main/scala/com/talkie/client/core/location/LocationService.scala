package com.talkie.client.core.location

import android.location.{ LocationListener => Listener }
import com.talkie.client.common.services.{ Service => GenericService }
import com.talkie.client.core.location.LocationProviders.{ LocationProvider => Provider }

import scalaz.Free

sealed trait LocationService[R] extends GenericService[R]
final case class CheckLastKnownLocation(providers: Provider*) extends LocationService[Boolean]
final case class RegisterLocationListener(listener: Listener, providers: Provider*) extends LocationService[Boolean]
final case class RemoveLocationListener(listener: Listener) extends LocationService[Boolean]

trait LocationServiceFrees[S[R] >: LocationService[R]] {

  def checkLastKnownLocation(providers: Provider*): Free[S, Boolean] =
    Free.liftF(CheckLastKnownLocation(providers: _*): S[Boolean])

  def registerLocationListener(listener: Listener, providers: Provider*): Free[S, Boolean] =
    Free.liftF(RegisterLocationListener(listener, providers: _*): S[Boolean])

  def removeLocationListener(listener: Listener): Free[S, Boolean] =
    Free.liftF(RemoveLocationListener(listener): S[Boolean])
}

object LocationService extends LocationServiceFrees[LocationService]
object Service extends LocationServiceFrees[GenericService]
