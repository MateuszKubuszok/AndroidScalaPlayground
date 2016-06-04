package com.talkie.client.core.permissions

import android.app.Activity
import android.content.pm.PackageManager
import com.talkie.client.common.context.Context
import com.talkie.client.core.permissions.RequiredPermissions._

import scala.concurrent.duration.Duration
import scala.concurrent.{ Await, Promise }

private[permissions] final class PermissionActions(context: Context, requestor: Activity) {

  private val logger = context loggerFor this

  def isPermissionGranted(permission: RequiredPermission): Boolean =
    context.androidContext.checkSelfPermission(permission.toString) == PackageManager.PERMISSION_GRANTED

  def checkPermissions(permissions: Seq[RequiredPermission]): PermissionStatusMap =
    permissions map { permission =>
      permission -> (if (isPermissionGranted(permission)) PermissionStatuses.Granted else PermissionStatuses.Denied)
    } toMap

  def requestPermissions(permissions: Seq[RequiredPermission]): (PermissionStatusMap, Int) = {
    val (granted, denied) = permissions partition isPermissionGranted

    val statuses: PermissionStatusMap = (granted map { permission =>
      permission -> PermissionStatuses.Granted
    }) ++ (denied map { permission =>
      permission -> PermissionStatuses.Pending
    }) toMap

    if (denied.nonEmpty) {
      statuses -> context.withSharedStateUpdated { state =>
        val requestId = scala.util.Random.nextInt(Int.MaxValue)
        val resultP = Promise[Unit]()
        val newRequests = state.permissionRequests + (requestId -> resultP)
        val newState = state.copy(permissionRequests = newRequests)

        requestor.requestPermissions(denied map (_.toString) toArray, requestId) // TODO: handle that in activities

        newState -> requestId
      }
    } else {
      statuses -> -1 // TODO: replace with Option
    }
  }

  def requirePermissions(permissions: Seq[RequiredPermission]): PermissionStatusMap = {
    if (permissions.nonEmpty) {
      val (_, requestId) = requestPermissions(permissions)

      val requestP = context.withSharedState { _.permissionRequests.get(requestId) }

      requestP foreach { request =>
        logger trace "Start waiting for granting permissions"

        Await.result(request.future, Duration.Inf)

        context.withSharedStateUpdated { state =>
          val newRequests = state.permissionRequests - requestId
          state.copy(permissionRequests = newRequests) -> ((): Unit)
        }
      }
    }

    checkPermissions(permissions)
  }
}
