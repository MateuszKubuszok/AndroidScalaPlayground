package com.talkie.client.core.services

import scalaz._
import scalaz.concurrent.Task

trait ServiceInterpreter extends (Service ~> Task)

object ServiceInterpreter {

  implicit class GeneralizeService[SpecificService[_] <: Service[_], R](specification: Free[SpecificService, R]) {

    def fireAndForget()(implicit serviceInterpreter: ServiceInterpreter): Unit = interpret.fireAndForget()

    def fireAndWait()(implicit serviceInterpreter: ServiceInterpreter): \/[Throwable, R] = interpret.fireAndWait()

    def generalize: Free[Service, R] = specification.asInstanceOf[Free[Service, R]]

    def interpret(implicit serviceInterpreter: ServiceInterpreter): Task[R] = generalize.interpret
  }

  implicit class ServiceInvoker[R](serviceFree: Free[Service, R]) {

    def fireAndForget()(implicit serviceInterpreter: ServiceInterpreter): Unit = interpret.fireAndForget()

    def fireAndWait()(implicit serviceInterpreter: ServiceInterpreter): \/[Throwable, R] = interpret.fireAndWait()

    def interpret(implicit serviceInterpreter: ServiceInterpreter): Task[R] = serviceFree.foldMap(serviceInterpreter)
  }

  implicit class TaskRunner[R](task: Task[R]) {

    def fireAndForget(): Unit = task.unsafePerformAsyncInterruptibly(_ => ())

    def fireAndWait(): \/[Throwable, R] = task.unsafePerformSyncAttempt
  }
}
