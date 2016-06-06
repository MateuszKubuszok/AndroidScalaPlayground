package com.talkie.client.common.services

import scalaz.Free

trait Service[R]

object Service {

  def doNothing: Free[Service, Unit] = Free.point(())
}
