package com.talkie.client.domain.services.location

import android.app.Activity
import android.content.Context.LOCATION_SERVICE
import android.location.{ Criteria, LocationManager }
import com.talkie.client.core.logging.LoggerComponent
import com.talkie.client.core.permissions.PermissionsMessages.RequirePermissionsRequest
import com.talkie.client.core.permissions.RequiredPermissions.AccessFineLocation
import com.talkie.client.core.permissions.{ RequiredPermissions, PermissionsServicesComponent }
import com.talkie.client.core.services.{ Context, Service, AsyncService }
import com.talkie.client.domain.services.location.LocationMessages._

import scala.concurrent.duration._
import scala.util.{ Success, Failure, Try }

trait LocationServices {

  def getLastKnownLocation: AsyncService[LastKnownLocationRequest, LastKnownLocationResponse]
  def registerLocationListener: AsyncService[RegisterLocationListenerRequest, RegisterLocationListenerResponse]
  def removeLocationListener: AsyncService[RemoveLocationListenerRequest, RemoveLocationListenerResponse]
}

trait LocationServicesComponent {

  def locationServices: LocationServices
}

trait LocationServicesComponentImpl extends LocationServicesComponent {
  self: Activity with LoggerComponent with PermissionsServicesComponent =>

  object locationServices extends LocationServices {

    private lazy val locationManager = getSystemService(LOCATION_SERVICE).asInstanceOf[LocationManager]

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

    override val getLastKnownLocation = Service { (request: LastKnownLocationRequest, context: Context) =>
      logger trace s"Requested last known location using: ${request.provider}"

      implicit val c = context
      implicit val ec = context.executionContext

      for {
        response <- permissionsServices.requirePermissions(RequirePermissionsRequest(Set(AccessFineLocation)))
        if response.granted
      } yield {
        val locationOpt = {
          Option(locationManager.getBestProvider(enabledProviderCriteria, true))
        } orElse {
          Option(locationManager.getBestProvider(disabledProviderCriteria, false))
        } flatMap { provider =>
          Option(locationManager.getLastKnownLocation(request.provider.toString))
        }

        logger trace s"Last known location resolved to: $locationOpt"

        LastKnownLocationResponse(locationOpt)
      }
    }

    override val registerLocationListener = Service { (request: RegisterLocationListenerRequest, context: Context) =>
      logger trace s"Requested LocationListener registration for: ${request.providers mkString ", "}"

      implicit val c = context
      implicit val ec = context.executionContext

      for {
        response <- permissionsServices.requirePermissions(RequirePermissionsRequest(Set(AccessFineLocation)))
        if response.granted
      } yield {
        val success = request.providers.nonEmpty && (request.providers map { provider =>
          logger trace s"Registering LocationListener on: $provider"
          locationManager.requestLocationUpdates(provider.toString, minTimeMs, minDistanceM, request.listener)
          locationManager.isProviderEnabled(provider.toString)
        } forall identity)

        logger trace s"Listener registration successful: $success"
        RegisterLocationListenerResponse(success)
      }
    }

    override val removeLocationListener = Service.async { request: RemoveLocationListenerRequest =>
      logger trace "Requested LocationListener removal"

      locationManager.removeUpdates(request.listener)

      RemoveLocationListenerResponse()
    }
  }
}