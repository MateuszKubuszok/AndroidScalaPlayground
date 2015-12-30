package com.talkie.client.core.permissions

import android.app.Activity
import android.content.pm.PackageManager
import com.talkie.client.core.logging.LoggerComponent
import com.talkie.client.core.permissions.PermissionsMessages._
import com.talkie.client.core.permissions.RequiredPermissions.RequiredPermissions
import com.talkie.client.core.services.{AsyncService, Context, Service}

import scala.collection.mutable
import scala.concurrent.{Future, Promise}

trait PermissionsServices {

  def checkPermissions: AsyncService[CheckPermissionsRequest, CheckPermissionsResponse]
  def requestPermissions: AsyncService[RequestPermissionsRequest, RequestPermissionsResponse]
  def requirePermissions: AsyncService[RequirePermissionsRequest, RequirePermissionsResponse]
}

trait PermissionsServicesComponent {

  def permissionsServices: PermissionsServices
}

object PermissionsServicesComponentImpl {

  val requests = mutable.Map[Int, Promise[Unit]]()
}

trait PermissionsServicesComponentImpl extends Activity with PermissionsServicesComponent {
  self: Activity
    with LoggerComponent =>

  import PermissionsServicesComponentImpl._

  override def onRequestPermissionsResult(requestCode: Int, permissions: Array[String], grantResults: Array[Int]) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    logger trace s"Permission results arrived for request $requestCode: ${permissions.toList mkString ","}"
    requests get requestCode map { promise =>
      requests remove requestCode
      promise.success()
    } orElse {
      logger assertionFailed s"Permission request $requestCode should have been set within requests map"
      None
    }
  }

  object permissionsServices extends PermissionsServices {

    override val checkPermissions = Service.async { request: CheckPermissionsRequest =>
      logger trace s"Checking permissions: ${request.permissions mkString ", "}"

      CheckPermissionsResponse(request.permissions forall isPermissionGranted)
    }

    override val requestPermissions = Service.async { request: RequestPermissionsRequest =>
      logger trace s"Requested permissions: ${request.permissions mkString ", "}"

      val (granted, denied) = request.permissions partition isPermissionGranted

      val requestId = if (denied.nonEmpty) {
        val requestId = scala.util.Random.nextInt()
        val resultP = Promise[Unit]()
        requests.put(requestId, resultP)
        self.requestPermissions(denied map (_.toString) toArray, requestId)
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

    override val requirePermissions = Service { (request: RequirePermissionsRequest, context: Context) =>
      logger trace s"Requiring permissions: ${request.permissions mkString ", "}"

      implicit val c = context
      implicit val ec = PermissionsExecutionContext

      for {
        resultsOnRequest <- requestPermissions(RequestPermissionsRequest(request.permissions))

        waitIfPending <- resultsOnRequest.requestId flatMap requests.get map { promise =>
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

    private def isPermissionGranted(permission: RequiredPermissions) =
      checkSelfPermission(permission.toString) == PackageManager.PERMISSION_GRANTED
  }
}