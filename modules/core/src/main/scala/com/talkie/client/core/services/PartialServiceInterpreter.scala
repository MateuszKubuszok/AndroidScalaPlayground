package com.talkie.client.core.services

import scala.reflect.ClassTag
import scalaz.concurrent.Task
import scalaz.{ Monad, \/, Free, ~> }

abstract class PartialServiceInterpreter[From[_] <: Service[_], To[_]](implicit classTag: ClassTag[From[_]])
    extends (From ~> To) {

  def forService[R]: PartialFunction[Service[R], To[R]] = {
    case service if classTag.runtimeClass.isInstance(service) => apply(service.asInstanceOf[From[R]])
  }
}

trait PartialServiceInterpreterCompanion[From[_] <: Service[_], To[_]] {

  type PSI = PartialServiceInterpreter[From, To]

  implicit class PartialServiceInvoker[R](partialServiceFree: Free[From, R]) {

    def interpret(implicit psi: PSI, M: Monad[To]): To[R] = partialServiceFree.foldMap(psi)
  }

  implicit class PartialServiceRunner[R](partialServiceFree: Free[From, R]) {

    import ServiceInterpreter.TaskRunner

    def fireAndForget()(implicit psi: PSI, M: Monad[To]): Unit =
      partialServiceFree.interpret.asInstanceOf[Task[R]].fireAndForget()

    def fireAndWait()(implicit psi: PSI, M: Monad[To]): \/[Throwable, R] =
      partialServiceFree.interpret.asInstanceOf[Task[R]].fireAndWait()
  }
}
