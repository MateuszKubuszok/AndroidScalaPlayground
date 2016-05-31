package com.talkie.client.core.services

import scalaz._
import scalaz.concurrent.Task

trait ServiceInterpreter extends (Service ~> Task)

object ServiceInterpreter {

  implicit class GeneralizeService[SpecificService[_] <: Service[_], R](specification: Free[SpecificService, R]) {

    def generalize: Free[Service, R] = specification.asInstanceOf[Free[Service, R]]
  }
}
