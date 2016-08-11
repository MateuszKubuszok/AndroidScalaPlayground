package com.talkie.client.common.shared.services

import android.os.Binder

private[services] trait SharedServiceBinder[Service <: SharedService[Service]] extends Binder {

  val service: Service
}
