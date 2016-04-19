package com.talkie.client.core.events

import com.talkie.client.core.events.EventMessages._
import com.talkie.client.core.services.{ AsyncService, Context, Service, SyncService }

import scala.collection.mutable
import scala.reflect.ClassTag
import scala.util.Try

trait EventBus {

  def getListeners: SyncService[GetListenersRequest[_ <: Event], GetListenersResponse]
  def notifyEventListeners: AsyncService[NotifyEventListenersRequest[_ <: Event], NotifyEventListenersResponse]
  def registerEventListener: SyncService[RegisterEventListenerRequest[_ <: Event], RegisterEventListenerResponse]
  def removeEventListener: SyncService[RemoveEventListenerRequest[_ <: Event], RemoveEventListenerResponse]
}

class EventBusImpl extends EventBus {

  protected def listenersOf[E <: Event](eventType: ClassTag[E], context: Context) =
    context.eventListeners.getOrElseUpdate(eventType, mutable.Set()).asInstanceOf[mutable.Set[EventListener[E]]]

  override val getListeners = Service { (request: GetListenersRequest[_ <: Event], context: Context) =>
    GetListenersResponse(listenersOf(request.eventType, context).toSet)
  }

  override val notifyEventListeners = Service.async {
    (request: NotifyEventListenersRequest[_ <: Event], context: Context) =>
      context.loggerFor(this) trace s"Notify listeners about event: ${request.event}"

      def notifyFor[E <: Event](typedRequest: NotifyEventListenersRequest[E]) = for {
        listener <- listenersOf(typedRequest.eventType, context)
      } yield Try(listener.handleEvent(typedRequest.event)).isSuccess

      val results = notifyFor(request)

      NotifyEventListenersResponse(results forall identity)
  }

  override val registerEventListener = Service {
    (request: RegisterEventListenerRequest[_ <: Event], context: Context) =>

      def addListener[E <: Event](request: RegisterEventListenerRequest[E]) =
        listenersOf(request.eventType, context).add(request.listener)

      RegisterEventListenerResponse(addListener(request))
  }

  override val removeEventListener = Service { (request: RemoveEventListenerRequest[_ <: Event], context: Context) =>

    def removeListener[E <: Event](request: RemoveEventListenerRequest[E]) =
      listenersOf(request.eventType, context).remove(request.listener)

    RemoveEventListenerResponse(removeListener(request))
  }
}
