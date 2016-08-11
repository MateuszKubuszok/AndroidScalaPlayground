package com.talkie.client.core.location

import scalaz.~>
import scalaz.concurrent.Task

final class LocationServiceTaskInterpreter(implicit locationActions: LocationActions)
    extends (LocationService ~> Task) {

  import LocationService._

  override def apply[R](in: LocationService[R]): Task[R] = in match {

    case CheckLastKnownLocation() => Task {
      locationActions.checkLastKnownLocation().asInstanceOf[R]
    }

    case RegisterLocationListener(listener, providers @ _*) => Task {
      locationActions.registerLocationListener(listener, providers: _*).asInstanceOf[R]
    }

    case RemoveLocationListener(listener) => Task {
      locationActions.removeLocationListener(listener).asInstanceOf[R]
    }
  }
}
