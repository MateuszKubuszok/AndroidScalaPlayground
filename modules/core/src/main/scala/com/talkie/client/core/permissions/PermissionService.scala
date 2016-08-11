package com.talkie.client.core.permissions

import com.talkie.client.core.permissions.RequiredPermissions.RequiredPermission

import scalaz.{ :<:, Free }

sealed trait PermissionService[R]

object PermissionService {

  final case class CheckPermissions(permissions: RequiredPermission*) extends PermissionService[PermissionStatusMap]
  final case class RequirePermissions(permissions: RequiredPermission*) extends PermissionService[PermissionStatusMap]

  class Ops[S[_]](implicit s0: PermissionService :<: S) {

    def checkPermissions(permissions: RequiredPermission*): Free[S, PermissionStatusMap] =
      Free.liftF(s0.inj(CheckPermissions(permissions: _*)))

    def requirePermissions(permissions: RequiredPermission*): Free[S, PermissionStatusMap] =
      Free.liftF(s0.inj(RequirePermissions(permissions: _*)))
  }
}
