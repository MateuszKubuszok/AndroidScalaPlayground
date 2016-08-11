package com.talkie.client.core

import com.talkie.client.core.permissions.RequiredPermissions._
import com.talkie.client.core.permissions.PermissionStatuses.PermissionStatus

package object permissions {

  type PermissionStatusMap = Map[RequiredPermission, PermissionStatus]

  implicit class WithPermissions(permissions: PermissionStatusMap) {

    def ensuringPermissions[T](ensured: RequiredPermission*)(block: => T): T = {

      val failed = ensured.toSet.filter { checked =>
        permissions.getOrElse(checked, PermissionStatuses.Denied) != PermissionStatuses.Granted
      }

      if (failed.nonEmpty) throw PermissionException(failed.toSeq: _*)
      else block
    }
  }
}
