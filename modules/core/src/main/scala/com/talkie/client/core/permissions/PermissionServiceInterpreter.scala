package com.talkie.client.core.permissions

import android.app.Activity
import com.talkie.client.common.context.Context
import com.talkie.client.common.services.{ ~@~>, ~&~> }

import scalaz.concurrent.Task

trait PermissionServiceInterpreter extends (PermissionService ~@~> Task)

object PermissionServiceInterpreter extends (PermissionService ~&~> Task)

final class PermissionServiceInterpreterImpl(context: Context, requestor: Activity)
    extends PermissionServiceInterpreter {

  private val actions = new PermissionActions(context, requestor)
  private val logger = context loggerFor this

  def apply[R](in: PermissionService[R]): Task[R] = in match {

    case CheckPermissions(permissions @ _*) => Task {
      logger trace s"Checking permissions: ${permissions mkString ", "}"
      actions.checkPermissions(permissions).asInstanceOf[R]
    }

    case RequestPermissions(permissions @ _*) => Task {
      logger trace s"Requested permissions: ${permissions mkString ", "}"
      actions.requestPermissions(permissions).asInstanceOf[R]
    }

    case RequirePermissions(permissions @ _*) => Task {
      logger trace s"Requiring permissions: ${permissions mkString ", "}"
      actions.requirePermissions(permissions).asInstanceOf[R]
    }
  }
}
