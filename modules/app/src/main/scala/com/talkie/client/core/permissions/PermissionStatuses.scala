package com.talkie.client.core.permissions

import android.content.pm.PackageManager._

object PermissionStatuses extends Enumeration {
  type PermissionStatuses = Value
  val Denied = Value(PERMISSION_DENIED)
  val Granted = Value(PERMISSION_GRANTED)
  val Pending = Value(1)
}
