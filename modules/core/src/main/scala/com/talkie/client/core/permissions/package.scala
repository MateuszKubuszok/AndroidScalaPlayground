package com.talkie.client.core

import com.talkie.client.core.permissions.RequiredPermissions._
import com.talkie.client.core.permissions.PermissionStatuses.PermissionStatus

import scalaz.concurrent.Task

package object permissions {

  type PermissionStatusMap = Map[RequiredPermission, PermissionStatus]

  implicit class WithPermissions(permissionsT: Task[PermissionStatusMap]) {

    def ensuring[T](ensured: RequiredPermission*)(block: => T): Task[T] = permissionsT.map { permissions =>

      val failed = ensured.toSet.filter { checked =>
        permissions.getOrElse(checked, PermissionStatuses.Denied) != PermissionStatuses.Granted
      }

      if (failed.nonEmpty) throw PermissionException(failed.toSeq:_*)
      else block
    }
  }
}
