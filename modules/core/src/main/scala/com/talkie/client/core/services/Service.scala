package com.talkie.client.core.services

import com.talkie.client.core.context.Context

import scala.concurrent.Future

trait Service[Request, Response, C <: Context] {

  def apply(request: Request)(implicit context: C): Response
}

trait SyncService[Request, Response, C <: Context] extends Service[Request, Response, C]

trait AsyncService[Request, Response, C <: Context] extends Service[Request, Future[Response], C]

object Service {

  def apply[Request, Response](fun: Request => Response) = new SyncServiceImpl(monitored(withoutContext(fun)))
  def apply[Request, Response, C <: Context](fun: (Request, C) => Response) = new SyncServiceImpl(monitored(fun))

  def apply[Request, Response](fun: Request => Future[Response]) = new AsyncServiceImpl(monitored(withoutContext(fun)))
  def apply[Request, Response, C <: Context](fun: (Request, C) => Future[Response]) = new AsyncServiceImpl(monitored(fun))

  def async[Request, Response](fun: Request => Response) = new AsyncServiceImpl(asAsync(monitored(withoutContext(fun))))
  def async[Request, Response, C <: Context](fun: (Request, C) => Response) = new AsyncServiceImpl(asAsync(monitored(fun)))

  private def asAsync[Request, Response, C <: Context](fun: (Request, C) => Response) =
    (request: Request, context: C) => {
      implicit val serviceContext = context.serviceExecutionContext
      Future(fun(request, context))
    }

  private def monitored[Request, Response, C <: Context](fun: (Request, C) => Response) =
    (request: Request, context: C) => try {
      fun(request, context)
    } catch {
      case e: Throwable =>
        context.loggerFor(this) error ("Exception thrown within a service", e)
        throw e
    }

  private def withoutContext[Request, Response](fun: Request => Response) =
    (request: Request, _: Context) => fun(request)

  class SyncServiceImpl[Request, Response, C <: Context](fun: (Request, C) => Response)
      extends SyncService[Request, Response, C] {

    def apply(request: Request)(implicit context: C): Response = fun(request, context)
  }

  class AsyncServiceImpl[Request, Response, C <: Context](fun: (Request, C) => Future[Response])
      extends AsyncService[Request, Response, C] {

    def apply(request: Request)(implicit context: C): Future[Response] = fun(request, context)
  }
}
