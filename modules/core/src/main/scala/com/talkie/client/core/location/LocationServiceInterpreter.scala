package com.talkie.client.core.location

import android.content.Context._
import android.location.{ LocationListener => Listener, LocationManager => Manager, _ }
import com.talkie.client.core.context.Context
import com.talkie.client.core.location.LocationProviders.{ LocationProvider => Provider }
import com.talkie.client.core.permissions.{ PermissionService, PermissionServiceInterpreter, RequiredPermissions }
import com.talkie.client.core.services.~@~>

import scala.concurrent.duration._
import scalaz.concurrent.Task

trait LocationServiceInterpreter extends (LocationService ~@~> Task)

final class LocationServiceInterpreterImpl(
    context: Context,
    permissionServiceInterpreter: PermissionServiceInterpreter
  ) extends LocationServiceInterpreter {

  import PermissionService._

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
        .foldMap(permissionServiceInterpreter)
        .ensuring(requiredPermission) {
      lastKnownLocation(provider).asInstanceOf[R]
    }

    case RegisterLocationListener(listener, providers @ _*) => requirePermissions(requiredPermission)
        .foldMap(permissionServiceInterpreter)
        .ensuring(requiredPermission) {
      registerLocationListener(listener, providers: _*).asInstanceOf[R]
    }

    case RemoveLocationListener(listener) => Task {
      removeLocationListener(listener).asInstanceOf[R]
    }
  }

  private def lastKnownLocation(provider: Provider): Option[Location] = {
    logger trace s"Requested last known location using: $provider for fallback"

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
