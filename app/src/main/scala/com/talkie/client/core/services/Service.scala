package com.talkie.client.core.services

import scala.concurrent.Future

trait Service[Request, Response] {

  def apply(request: Request)(implicit context: Context): Response
}

trait SyncService[Request, Response] extends Service[Request, Response]

trait AsyncService[Request, Response] extends Service[Request, Future[Response]]

object Service {

  def apply[Request, Response](fun: Request => Response) = new SyncServiceImpl(withoutContext(fun))
  def apply[Request, Response](fun: (Request, Context) => Response) = new SyncServiceImpl(fun)

  def apply[Request, Response](fun: Request => Future[Response]) = new AsyncServiceImpl(withoutContext(fun))
  def apply[Request, Response](fun: (Request, Context) => Future[Response]) = new AsyncServiceImpl(fun)

  def async[Request, Response](fun: Request => Response) = new AsyncServiceImpl(asAsync(withoutContext(fun)))
  def async[Request, Response](fun: (Request, Context) => Response) = new AsyncServiceImpl(asAsync(fun))

  private def asAsync[Request, Response](fun: (Request, Context) => Response) =
    (request: Request, context: Context) => {
      implicit val serviceContext = context.executionContext
      Future(fun(request, context))
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
