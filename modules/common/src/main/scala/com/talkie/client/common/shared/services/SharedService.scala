package com.talkie.client.common.shared.services

import android.content.Intent
import android.os.IBinder

import com.talkie.client.common.components.{ Service => ComponentService }

trait SharedService[Service <: SharedService[Service]] extends ComponentService { self: Service =>

  val binder: IBinder = new SharedServiceBinder[Service] {

    val service: Service = self
  }

  override def onBind(intent: Intent): IBinder = binder
}
