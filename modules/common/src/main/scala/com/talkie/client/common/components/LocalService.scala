package com.talkie.client.common.components

import android.content.Intent
import android.os.{ Binder, IBinder }

trait LocalService extends Service {

  def onBind(intent: Intent): IBinder = LocalBinder

  private object LocalBinder extends Binder {

    def service: LocalService = LocalService.this
  }
}
