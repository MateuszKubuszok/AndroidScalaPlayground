package com.talkie.client.core.permissions

import com.talkie.client.common.services.{ Service => GenericService }
import com.talkie.client.core.permissions.RequiredPermissions.RequiredPermission

import scalaz.Free

sealed trait PermissionService[R] extends GenericService[R]
final case class CheckPermissions(permissions: RequiredPermission*) extends PermissionService[PermissionStatusMap]
final case class RequestPermissions(permissions: RequiredPermission*)
  extends PermissionService[(PermissionStatusMap, Int)]
final case class RequirePermissions(permissions: RequiredPermission*) extends PermissionService[PermissionStatusMap]

trait PermissionServiceFrees[S[R] >: PermissionService[R]] {

  def checkPermissions(permissions: RequiredPermission*): Free[S, PermissionStatusMap] =
    Free.liftF(CheckPermissions(permissions: _*): S[PermissionStatusMap])

  def requestPermissions(permissions: RequiredPermission*): Free[S, (PermissionStatusMap, Int)] =
    Free.liftF(RequestPermissions(permissions: _*): S[(PermissionStatusMap, Int)])

  def requirePermissions(permissions: RequiredPermission*): Free[S, PermissionStatusMap] =
    Free.liftF(RequirePermissions(permissions: _*): S[PermissionStatusMap])
}

object PermissionService extends PermissionServiceFrees[PermissionService]
object Service extends PermissionServiceFrees[GenericService]
