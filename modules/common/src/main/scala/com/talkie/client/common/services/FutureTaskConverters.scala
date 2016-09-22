package com.talkie.client.common.services

import scala.concurrent.{ ExecutionContext, Future, Promise }
import scala.util.{ Failure, Success }
import scalaz._
import Scalaz._
import scalaz.concurrent.Task

object FutureTaskConverters {

  implicit def future2task[T](future: Future[T])(implicit ec: ExecutionContext): Task[T] = {
    Task.async { register =>
      future.onComplete {
        case Success(v)  => register(v.right)
        case Failure(ex) => register(ex.left)
      }
    }
  }

  implicit def task2future[T](task: Task[T]): Future[T] = {
    val p: Promise[T] = Promise()

    task.unsafePerformAsync {
      case -\/(ex) => p.failure(ex)
      case \/-(r)  => p.success(r)
    }

    p.future
  }

  implicit class TaskEnhancer[T](task: Task[T]) {

    def asScala: Future[T] = task2future(task)
  }

  //  implicit def future2FutureEnhancer[T](fut: Future[T])(implicit ec: ExecutionContext): FutureEnhancer[T] =
  //    FutureEnhancer(fut)(ec)

  implicit class FutureEnhancer[T](fut: Future[T])(implicit ec: ExecutionContext) {

    def asTask: Task[T] = future2task(fut)(ec)
  }
}
