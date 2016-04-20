package com.talkie.client.core.permissions

import android.content.pm.PackageManager
import com.talkie.client.core.context.{ Context, CoreContext }
import com.talkie.client.core.permissions.PermissionsMessages._
import com.talkie.client.core.permissions.RequiredPermissions.RequiredPermissions
import com.talkie.client.core.services.{ AsyncService, Service }

import scala.concurrent.{ Future, Promise }

trait PermissionsServices {

  type PermissionsContext = Context with CoreContext

  def checkPermissions: AsyncService[CheckPermissionsRequest, CheckPermissionsResponse, PermissionsContext]
  def requestPermissions: AsyncService[RequestPermissionsRequest, RequestPermissionsResponse, PermissionsContext]
  def requirePermissions: AsyncService[RequirePermissionsRequest, RequirePermissionsResponse, PermissionsContext]
}

class PermissionsServicesImpl extends PermissionsServices {

  override val checkPermissions = Service.async { (request: CheckPermissionsRequest, context: PermissionsContext) =>
    context.loggerFor(this) trace s"Checking permissions: ${request.permissions mkString ", "}"

    CheckPermissionsResponse(request.permissions forall isPermissionGranted(context))
  }

  override val requestPermissions = Service.async { (request: RequestPermissionsRequest, context: PermissionsContext) =>
    context.loggerFor(this) trace s"Requested permissions: ${request.permissions mkString ", "}"

    val (granted, denied) = request.permissions partition isPermissionGranted(context)

    val requestId = if (denied.nonEmpty) {
      val requestId = scala.util.Random.nextInt()
      val resultP = Promise[Unit]()
      context.permissionsRequests.put(requestId, resultP)
      request.requestor.requestPermissions(denied map (_.toString) toArray, requestId)
      Some(requestId)
    } else None

    RequestPermissionsResponse(
      (granted map { permission =>
        (permission, PermissionStatuses.Granted)
      }) ++ (denied map { permission =>
        (permission, PermissionStatuses.Pending)
      }) toMap,
      requestId
    )
  }

  override val requirePermissions = Service { (request: RequirePermissionsRequest, context: PermissionsContext) =>
    val logger = context.loggerFor(this)

    logger trace s"Requiring permissions: ${request.permissions mkString ", "}"

    implicit val c = context
    implicit val ec = context.permissionsExecutionContext

    for {
      resultsOnRequest <- requestPermissions(RequestPermissionsRequest(request.requestor, request.permissions))

      waitIfPending <- resultsOnRequest.requestId flatMap context.permissionsRequests.get map { promise =>
        val future = promise.future
        logger trace "Start waiting for granting permissions"
        future foreach { _ => logger trace "Stopped waiting for granting permissions" }
        future
      } getOrElse Future.successful(true)

      verifyPending <- checkPermissions(CheckPermissionsRequest(request.permissions))
    } yield {
      logger trace s"Required permissions: ${request.permissions mkString ", "} with result: ${verifyPending.granted}"
      RequirePermissionsResponse(verifyPending.granted)
    }
  }

  private def isPermissionGranted(context: Context)(permission: RequiredPermissions) =
    context.androidContext.checkSelfPermission(permission.toString) == PackageManager.PERMISSION_GRANTED
}
