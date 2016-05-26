package com.talkie.client.core

import com.talkie.client.core.permissions.RequiredPermissions._
import com.talkie.client.core.permissions.PermissionStatuses.PermissionStatus

package object permissions {

  type PermissionStatusMap = Map[RequiredPermission, PermissionStatus]
}
