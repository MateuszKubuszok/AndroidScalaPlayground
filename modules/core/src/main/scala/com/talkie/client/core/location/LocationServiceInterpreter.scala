package com.talkie.client.core.location

import android.content.Context._
import android.location.{ LocationListener => Listener, LocationManager => Manager, _ }
import com.talkie.client.common.context.Context
import com.talkie.client.core.location.LocationProviders.{ LocationProvider => Provider }
import com.talkie.client.core.permissions.{ PermissionServiceInterpreter, RequiredPermissions }
import com.talkie.client.core.permissions.PermissionService._
import com.talkie.client.core.permissions.PermissionServiceInterpreter._
import com.talkie.client.common.services.{ ~@~>, ~&~> }

import scala.concurrent.duration._
import scalaz.concurrent.Task

trait LocationServiceInterpreter extends (LocationService ~@~> Task)

object LocationServiceInterpreter extends (LocationService ~&~> Task)

final class LocationServiceInterpreterImpl(
    context:                                   Context,
    implicit val permissionServiceInterpreter: PermissionServiceInterpreter
) extends LocationServiceInterpreter {

  private val logger = context.loggerFor(this)

  private lazy val manager = context.androidContext.getSystemService(LOCATION_SERVICE).asInstanceOf[Manager]

  private val minTimeMs = (10 seconds).toMillis
  private val minDistanceM = 1500

  private lazy val disabledProviderCriteria = {
    val criteria = new Criteria
    criteria.setCostAllowed(false)
    criteria.setAccuracy(Criteria.ACCURACY_MEDIUM)
    criteria
  }
  private lazy val enabledProviderCriteria = {
    val criteria = new Criteria
    criteria.setCostAllowed(true)
    criteria.setAccuracy(Criteria.ACCURACY_FINE)
    criteria
  }

  private val requiredPermission = RequiredPermissions.AccessFineLocation

  override def apply[R](in: LocationService[R]): Task[R] = in match {

    case CheckLastKnownLocation(provider) => requirePermissions(requiredPermission)
      .interpret
      .ensuringPermissions(requiredPermission) {
        lastKnownLocation(provider).asInstanceOf[R]
      }

    case RegisterLocationListener(listener, providers @ _*) => requirePermissions(requiredPermission)
      .interpret
      .ensuringPermissions(requiredPermission) {
        registerLocationListener(listener, providers: _*).asInstanceOf[R]
      }

    case RemoveLocationListener(listener) => Task {
      removeLocationListener(listener).asInstanceOf[R]
    }
  }

  private def lastKnownLocation(provider: Provider): Option[Location] = {
    logger trace s"Requested last known location using: $provider for fallback"

    // TODO: this logic makes no sense...
    val locationOpt = Option {
      manager.getBestProvider(enabledProviderCriteria, true)
    } orElse Option {
      manager.getBestProvider(disabledProviderCriteria, false)
    } orElse Option {
      provider
    } flatMap { _ =>
      Option(manager.getLastKnownLocation(provider.toString))
    }

    logger trace s"Last known location resolved to: $locationOpt"

    locationOpt
  }

  private def registerLocationListener(listener: Listener, providers: Provider*): Boolean =
    providers.forall { provider =>
      logger trace s"Registering LocationListener on: $provider"
      manager.requestLocationUpdates(provider.toString, minTimeMs, minDistanceM, listener)
      manager.isProviderEnabled(provider.toString)
    }

  private def removeLocationListener(listener: Listener): Boolean = {
    logger trace "Requested LocationListener removal"
    manager.removeUpdates(listener)
    true // TODO: maybe check if we actually removed sth
  }
}
