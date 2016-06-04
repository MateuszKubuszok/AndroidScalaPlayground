package com.talkie.client.core.location

import com.talkie.client.common.context.Context
import com.talkie.client.core.permissions.{ PermissionServiceInterpreter }
import com.talkie.client.common.services.{ ~@~>, ~&~> }

import scalaz.concurrent.Task

trait LocationServiceInterpreter extends (LocationService ~@~> Task)

object LocationServiceInterpreter extends (LocationService ~&~> Task)

final class LocationServiceInterpreterImpl(
    context:                      Context,
    permissionServiceInterpreter: PermissionServiceInterpreter
) extends LocationServiceInterpreter {

  private val actions = new LocationActions(context, permissionServiceInterpreter)

  override def apply[R](in: LocationService[R]): Task[R] = in match {

    case CheckLastKnownLocation(provider) => actions.withPermission {
      actions.lastKnownLocation(provider).asInstanceOf[R]
    }

    case RegisterLocationListener(listener, providers @ _*) => actions.withPermission {
      actions.registerLocationListener(listener, providers: _*).asInstanceOf[R]
    }

    case RemoveLocationListener(listener) => Task {
      actions.removeLocationListener(listener).asInstanceOf[R]
    }
  }
}
