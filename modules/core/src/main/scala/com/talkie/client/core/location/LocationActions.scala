package com.talkie.client.core.location

import android.content.Context._
import android.location.{ LocationListener => Listener, LocationManager => Manager, _ }
import com.talkie.client.common.context.Context
import com.talkie.client.core.location.LocationProviders.{ LocationProvider => Provider }
import com.talkie.client.core.permissions.{ PermissionActions, RequiredPermissions }

import scala.concurrent.duration._

trait LocationActions {

  def checkLastKnownLocation(): Option[Location]

  def registerLocationListener(listener: Listener, providers: Provider*): Boolean

  def removeLocationListener(listener: Listener): Boolean
}

final class LocationActionsImpl(
    implicit
    context:           Context,
    permissionActions: PermissionActions
) extends LocationActions {

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

  private def withPermission[T](action: => T) = permissionActions
    .requirePermissions(requiredPermission)
    .ensuringPermissions(requiredPermission) {
      action
    }

  def checkLastKnownLocation(): Option[Location] = withPermission {
    logger trace s"Requested last known location fallback"

    val locationOpt = Option {
      manager.getBestProvider(enabledProviderCriteria, true)
    } orElse Option {
      manager.getBestProvider(disabledProviderCriteria, false)
    } flatMap { provider =>
      Option(manager.getLastKnownLocation(provider))
    }

    logger trace s"Last known location resolved to: $locationOpt"

    locationOpt
  }

  def registerLocationListener(listener: Listener, providers: Provider*): Boolean = withPermission {
    providers.forall { provider =>
      logger trace s"Registering LocationListener on: $provider"
      manager.requestLocationUpdates(provider.toString, minTimeMs, minDistanceM, listener)
      manager.isProviderEnabled(provider.toString)
    }
  }

  def removeLocationListener(listener: Listener): Boolean = {
    logger trace "Requested LocationListener removal"
    manager.removeUpdates(listener)
    true // TODO: maybe check if we actually removed sth
  }
}
