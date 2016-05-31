package com.talkie.client.core.services

import scala.reflect.ClassTag
import scalaz.~>

abstract class PartialServiceInterpreter[-From[_] <: Service[_], To[_]](implicit classTag: ClassTag[From])
    extends (From ~> To) {

  def forService[R]: PartialFunction[Service[R], To[R]] = {
    case service if classTag.runtimeClass.isInstance(service) => apply(service.asInstanceOf[From[R]])
  }
}
