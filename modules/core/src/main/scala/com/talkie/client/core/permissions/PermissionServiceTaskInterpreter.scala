package com.talkie.client.core.permissions

import scalaz.~>
import scalaz.concurrent.Task

final class PermissionServiceTaskInterpreter(implicit permissionActions: PermissionActions)
    extends (PermissionService ~> Task) {

  import PermissionService._

  def apply[R](in: PermissionService[R]): Task[R] = in match {

    case CheckPermissions(permissions @ _*) => Task {
      permissionActions.checkPermissions(permissions: _*).asInstanceOf[R]
    }

    case RequirePermissions(permissions @ _*) => Task {
      permissionActions.requirePermissions(permissions: _*).asInstanceOf[R]
    }
  }
}
