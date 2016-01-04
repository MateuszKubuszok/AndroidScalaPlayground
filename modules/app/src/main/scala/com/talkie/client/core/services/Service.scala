package com.talkie.client.core.services

import com.talkie.client.core.logging.LoggerComponentImpl

import scala.concurrent.Future

trait Service[Request, Response] {

  def apply(request: Request)(implicit context: Context): Response
}

trait SyncService[Request, Response] extends Service[Request, Response]

trait AsyncService[Request, Response] extends Service[Request, Future[Response]]

object Service extends LoggerComponentImpl {

  def apply[Request, Response](fun: Request => Response) = new SyncServiceImpl(monitored(withoutContext(fun)))
  def apply[Request, Response](fun: (Request, Context) => Response) = new SyncServiceImpl(monitored(fun))

  def apply[Request, Response](fun: Request => Future[Response]) = new AsyncServiceImpl(monitored(withoutContext(fun)))
  def apply[Request, Response](fun: (Request, Context) => Future[Response]) = new AsyncServiceImpl(monitored(fun))

  def async[Request, Response](fun: Request => Response) = new AsyncServiceImpl(asAsync(monitored(withoutContext(fun))))
  def async[Request, Response](fun: (Request, Context) => Response) = new AsyncServiceImpl(asAsync(monitored(fun)))

  private def asAsync[Request, Response](fun: (Request, Context) => Response) =
    (request: Request, context: Context) => {
      implicit val serviceContext = context.executionContext
      Future(fun(request, context))
    }

  private def monitored[Request, Response](fun: (Request, Context) => Response) =
    (request: Request, context: Context) => try {
      fun(request, context)
    } catch {
      case e: Throwable =>
        logger error ("Exception thrown within a service", e)
        throw e
    }

  private def withoutContext[Request, Response](fun: Request => Response) =
    (request: Request, _: Context) => fun(request)

  class SyncServiceImpl[Request, Response](fun: (Request, Context) => Response)
      extends SyncService[Request, Response] {

    def apply(request: Request)(implicit context: Context): Response = fun(request, context)
  }

  class AsyncServiceImpl[Request, Response](fun: (Request, Context) => Future[Response])
      extends AsyncService[Request, Response] {

    def apply(request: Request)(implicit context: Context): Future[Response] = fun(request, context)
  }
}
