package com.talkie.client.core.permissions

import android.R
import android.content.pm.PackageManager
import com.permissioneverywhere.{ PermissionResponse, PermissionResultCallback, PermissionEverywhere }
import com.talkie.client.common.context.Context
import com.talkie.client.core.permissions.RequiredPermissions._

import scala.concurrent.duration.Duration
import scala.concurrent.{ Await, Promise }

trait PermissionActions {

  def isPermissionGranted(permission: RequiredPermission): Boolean

  def checkPermissions(permissions: RequiredPermission*): PermissionStatusMap

  def requirePermissions(permissions: RequiredPermission*): PermissionStatusMap
}

final class PermissionActionsImpl(implicit context: Context) extends PermissionActions {

  private val logger = context loggerFor this

  def isPermissionGranted(permission: RequiredPermission): Boolean =
    context.androidContext.checkSelfPermission(permission.toString) == PackageManager.PERMISSION_GRANTED

  def checkPermissions(permissions: RequiredPermission*): PermissionStatusMap = {
    logger trace s"Checking permissions: ${permissions mkString ", "}"
    permissions map { permission =>
      permission -> (if (isPermissionGranted(permission)) PermissionStatuses.Granted else PermissionStatuses.Denied)
    } toMap
  }

  def requirePermissions(permissions: RequiredPermission*): PermissionStatusMap = {
    logger trace s"Requiring permissions: ${permissions mkString ", "}"

    val (granted, denied) = permissions partition isPermissionGranted

    if (denied.nonEmpty) {
      val requestId = scala.util.Random.nextInt(Int.MaxValue)
      val resultP = Promise[Unit]()

      PermissionEverywhere.getPermission(
        context.androidContext,
        denied map (_.toString) toArray,
        requestId,
        "Permission title", // TODO: fix this using reqources
        "Permission description", // TODO: fix using resources
        R.mipmap.sym_def_app_icon
      ).enqueue(new PermissionResultCallback {

          override def onComplete(permissionResponse: PermissionResponse): Unit = {
            logger debug s"Permission request result: ${permissionResponse.isGranted}"
            if (permissionResponse.isGranted) resultP.success(())
            else resultP.failure(new PermissionException(permissions: _*))
          }
        })

      Await.result(resultP.future, Duration.Inf)

      checkPermissions(permissions: _*)
    } else {
      (granted map { permission =>
        permission -> PermissionStatuses.Granted
      }) ++ (denied map { permission =>
        permission -> PermissionStatuses.Pending
      }) toMap
    }
  }
}
