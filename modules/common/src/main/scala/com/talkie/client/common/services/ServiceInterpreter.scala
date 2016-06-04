package com.talkie.client.common.services

import java.util.concurrent.atomic.AtomicBoolean

import com.talkie.client.common.logging.LoggerImpl

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

    val mockBoolean = new AtomicBoolean(false)

    def fireAndForget(): Unit = task.unsafePerformAsyncInterruptibly(logOnError, mockBoolean)

    def fireAndWait(): \/[Throwable, R] = {
      val result = task.unsafePerformSyncAttempt
      result.leftMap(logOnError)
      result
    }

    private def logOnError(error: Throwable): Unit =
      new LoggerImpl(this.getClass.getSimpleName) error (s"Task execution failed", error)

    private def logOnError(result: \/[Throwable, R]): Unit = result match {
      case -\/(error)  => logOnError(error)
      case \/-(result) => // ok
    }
  }
}
