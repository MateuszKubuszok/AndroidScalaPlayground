package com.talkie.client.domain.services.location

import android.app.Activity
import android.content.Context.LOCATION_SERVICE
import android.location.{Criteria, LocationManager}
import com.talkie.client.core.logging.LoggerComponent
import com.talkie.client.core.services.{Service, AsyncService}
import com.talkie.client.domain.services.location.LocationMessages._

import scala.concurrent.duration._

trait LocationServices {

  def getLastKnownLocation: AsyncService[LastKnownLocationRequest, LastKnownLocationResponse]
  def registerLocationListener: AsyncService[RegisterLocationListenerRequest, RegisterLocationListenerResponse]
  def removeLocationListener: AsyncService[RemoveLocationListenerRequest, RemoveLocationListenerResponse]
}

trait LocationServicesComponent {

  def locationServices: LocationServices
}

trait LocationServicesComponentImpl extends LocationServicesComponent {
  self: Activity
    with LoggerComponent =>

  object locationServices extends LocationServices {

    private lazy val locationManager = getSystemService(LOCATION_SERVICE).asInstanceOf[LocationManager]

    private val minTimeMs = (15 minutes).toMillis
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
      criteria.setAccuracy(Criteria.ACCURACY_HIGH)
      criteria
    }

    override val getLastKnownLocation = Service.async { request: LastKnownLocationRequest =>
      logger trace "Requested last known location"

      val locationOpt = {
        Option(locationManager.getBestProvider(enabledProviderCriteria, true))
      } orElse {
        Option(locationManager.getBestProvider(disabledProviderCriteria, false))
      } flatMap { provider =>
        Option(locationManager.getLastKnownLocation(request.provider.toString))
      }

      LastKnownLocationResponse(locationOpt)
    }

    override val registerLocationListener = Service.async { request: RegisterLocationListenerRequest =>
      logger trace "Requested LocationListener registration"

      request.providers foreach { provider =>
        locationManager.requestLocationUpdates(provider.toString, minTimeMs, minDistanceM, request.listener)
      }

      RegisterLocationListenerResponse(request.providers.nonEmpty)
    }

    override val removeLocationListener = Service.async { request: RemoveLocationListenerRequest =>
      logger trace "Requested LocationListener removal"

      locationManager.removeUpdates(request.listener)

      RemoveLocationListenerResponse()
    }
  }
}