package com.talkie.client.common.shared.services

import android.content.Context.BIND_AUTO_CREATE
import android.content.Intent
import com.talkie.client.common.components.Activity
import com.talkie.client.common.context.Context

import scala.reflect.ClassTag
import scala.util.Try

abstract class SharedServiceController[Service <: SharedService[Service]](
    context:  Context,
    activity: Activity
)(implicit serviceClass: ClassTag[Service]) {

  private val serviceConnection = new SharedServiceConnection(initializer, remover)
  private var serviceOpt: Option[Service] = None

  protected def initializer(service: Service): Unit = {
    serviceOpt = Some(service)
  }

  protected def remover(): Unit = {
    serviceOpt = None
  }

  val intent = new Intent(activity, serviceClass.runtimeClass)
  activity.bindService(intent, serviceConnection, BIND_AUTO_CREATE)

  activity.onDestroy {
    Try {
      activity.unbindService(serviceConnection)
    }.recover {
      case _: IllegalArgumentException => // nothing
      case e: Throwable                => context.loggerFor(this).error("Error during detaching service", e)
    }
  }
}
