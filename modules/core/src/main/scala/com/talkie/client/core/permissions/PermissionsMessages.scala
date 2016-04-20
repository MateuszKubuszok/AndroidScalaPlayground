package com.talkie.client.core.permissions

import android.app.Activity
import com.talkie.client.core.permissions.PermissionStatuses.PermissionStatuses
import com.talkie.client.core.permissions.RequiredPermissions.RequiredPermissions

object PermissionsMessages {

  case class CheckPermissionsRequest(permissions: Set[RequiredPermissions])
  case class CheckPermissionsResponse(granted: Boolean)

  case class RequestPermissionsRequest(requestor: Activity, permissions: Set[RequiredPermissions])
  case class RequestPermissionsResponse(granted: Map[RequiredPermissions, PermissionStatuses], requestId: Option[Int])

  case class RequirePermissionsRequest(requestor: Activity, permissions: Set[RequiredPermissions])
  case class RequirePermissionsResponse(granted: Boolean)
}
