package com.talkie.client.core.permissions

import com.talkie.client.core.permissions.PermissionStatuses.PermissionStatuses
import com.talkie.client.core.permissions.RequiredPermissions.RequiredPermissions

object PermissionsMessages {

  case class CheckPermissionsRequest(permissions: Set[RequiredPermissions])
  case class CheckPermissionsResponse(granted: Boolean)

  case class RequestPermissionsRequest(permissions: Set[RequiredPermissions])
  case class RequestPermissionsResponse(granted: Map[RequiredPermissions, PermissionStatuses], requestId: Option[Int])

  case class RequirePermissionsRequest(permissions: Set[RequiredPermissions])
  case class RequirePermissionsResponse(granted: Boolean)
}
