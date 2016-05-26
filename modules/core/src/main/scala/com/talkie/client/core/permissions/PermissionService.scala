package com.talkie.client.core.permissions

import com.talkie.client.core.permissions.RequiredPermissions.RequiredPermission
import com.talkie.client.core.services.Service

import scalaz.Free

sealed trait PermissionService[R] extends Service[R]
final case class CheckPermissions(permissions: RequiredPermission*) extends PermissionService[PermissionStatusMap]
final case class RequestPermissions(permissions: RequiredPermission*)
  extends PermissionService[(PermissionStatusMap, Int)]
final case class RequirePermissions(permissions: RequiredPermission*) extends PermissionService[PermissionStatusMap]

object PermissionService {

  def checkPermissions(permissions: RequiredPermission*): Free[PermissionService, PermissionStatusMap] =
    Free.liftF(CheckPermissions(permissions: _*): PermissionService[PermissionStatusMap])

  def requestPermissions(permissions: RequiredPermission*): Free[PermissionService, (PermissionStatusMap, Int)] =
    Free.liftF(RequestPermissions(permissions: _*): PermissionService[(PermissionStatusMap, Int)])

  def requirePermissions(permissions: RequiredPermission*): Free[PermissionService, PermissionStatusMap] =
    Free.liftF(RequirePermissions(permissions: _*): PermissionService[PermissionStatusMap])
}
