package com.talkie.client.common.shared.services

import android.content.{ ComponentName, ServiceConnection }
import android.os.IBinder

private[services] class SharedServiceConnection[Service <: SharedService[Service]](
    initializer: (Service) => Unit = (_: Service) => (),
    remover:     () => Unit        = () => ()
) extends ServiceConnection {

  override def onServiceConnected(name: ComponentName, binder: IBinder): Unit =
    initializer(binder.asInstanceOf[SharedServiceBinder[Service]].service)

  override def onServiceDisconnected(name: ComponentName): Unit =
    remover()
}
