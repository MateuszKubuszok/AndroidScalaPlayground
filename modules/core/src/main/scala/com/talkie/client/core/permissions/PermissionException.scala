package com.talkie.client.core.permissions

import com.talkie.client.core.permissions.RequiredPermissions.RequiredPermission

case class PermissionException(permissions: RequiredPermission*)
    extends RuntimeException(s"Failed to obtain permissions: ${permissions mkString ","}")
