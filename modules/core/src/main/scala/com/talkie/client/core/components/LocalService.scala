package com.talkie.client.core.components

import android.content.Intent
import android.os.{IBinder, Binder}

trait LocalService extends Service {

  def onBind(intent: Intent): IBinder = LocalBinder

  private object LocalBinder extends Binder {

    def service: LocalService = LocalService.this
  }
}
