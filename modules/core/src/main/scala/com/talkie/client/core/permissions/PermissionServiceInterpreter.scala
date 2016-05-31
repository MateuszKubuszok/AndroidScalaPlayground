package com.talkie.client.core.permissions

import android.app.Activity
import android.content.pm.PackageManager
import com.talkie.client.core.context.Context
import com.talkie.client.core.permissions.RequiredPermissions._
import com.talkie.client.core.services.{ ~@~>, ~&~> }

import scala.concurrent.duration.Duration
import scala.concurrent.{ Await, Promise }
import scalaz.concurrent.Task

trait PermissionServiceInterpreter extends (PermissionService ~@~> Task)

object PermissionServiceInterpreter extends (PermissionService ~&~> Task)

final class PermissionServiceInterpreterImpl(context: Context, requestor: Activity)
    extends PermissionServiceInterpreter {

  private val logger = context loggerFor this

  def apply[R](in: PermissionService[R]): Task[R] = in match {

    case CheckPermissions(permissions @ _*) => Task {
      logger trace s"Checking permissions: ${permissions mkString ", "}"
      checkPermissions(permissions).asInstanceOf[R]
    }

    case RequestPermissions(permissions @ _*) => Task {
      logger trace s"Requested permissions: ${permissions mkString ", "}"
      requestPermissions(permissions).asInstanceOf[R]
    }

    case RequirePermissions(permissions @ _*) => Task {
      logger trace s"Requiring permissions: ${permissions mkString ", "}"
      requirePermissions(permissions).asInstanceOf[R]
    }
  }

  private def isPermissionGranted(permission: RequiredPermission): Boolean =
    context.androidContext.checkSelfPermission(permission.toString) == PackageManager.PERMISSION_GRANTED

  private def checkPermissions(permissions: Seq[RequiredPermission]): PermissionStatusMap =
    permissions map { permission =>
      permission -> (if (isPermissionGranted(permission)) PermissionStatuses.Granted else PermissionStatuses.Denied)
    } toMap

  private def requestPermissions(permissions: Seq[RequiredPermission]): (PermissionStatusMap, Int) = {
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

  private def requirePermissions(permissions: Seq[RequiredPermission]): PermissionStatusMap = {
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
