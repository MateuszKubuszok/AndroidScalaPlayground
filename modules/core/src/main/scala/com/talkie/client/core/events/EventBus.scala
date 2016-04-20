package com.talkie.client.core.events

import com.talkie.client.core.context.{ CoreContext, Context }
import com.talkie.client.core.events.EventMessages._
import com.talkie.client.core.services.{ AsyncService, Service, SyncService }

import scala.collection.mutable
import scala.reflect.ClassTag
import scala.util.Try

trait EventBus {

  type EventContext = Context with CoreContext

  def getListeners: SyncService[GetListenersRequest[_ <: Event], GetListenersResponse, EventContext]
  def notifyEventListeners: AsyncService[NotifyEventListenersRequest[_ <: Event], NotifyEventListenersResponse, EventContext]
  def registerEventListener: SyncService[RegisterEventListenerRequest[_ <: Event], RegisterEventListenerResponse, EventContext]
  def removeEventListener: SyncService[RemoveEventListenerRequest[_ <: Event], RemoveEventListenerResponse, EventContext]
}

class EventBusImpl(context: Context) extends EventBus {

  private val logger = context.loggerFor(this)

  protected def listenersOf[E <: Event](eventType: ClassTag[E], context: EventContext) =
    context.eventListeners.getOrElseUpdate(eventType, mutable.Set()).asInstanceOf[mutable.Set[EventListener[E]]]

  override val getListeners = Service { (request: GetListenersRequest[_ <: Event], context: Context with CoreContext) =>
    GetListenersResponse(listenersOf(request.eventType, context).toSet)
  }

  override val notifyEventListeners = Service.async {
    (request: NotifyEventListenersRequest[_ <: Event], context: EventContext) =>
      logger trace s"Notify listeners about event: ${request.event}"

      def notifyFor[E <: Event](typedRequest: NotifyEventListenersRequest[E]) = for {
        listener <- listenersOf(typedRequest.eventType, context)
        _ = logger trace s"Notifying $listener about ${request.event}"
      } yield Try(listener.handleEvent(typedRequest.event)).isSuccess

      val results = notifyFor(request)

      NotifyEventListenersResponse(results forall identity)
  }

  override val registerEventListener = Service {
    (request: RegisterEventListenerRequest[_ <: Event], context: EventContext) =>
      logger trace s"Register listeners: ${request.listener}"

      def addListener[E <: Event](request: RegisterEventListenerRequest[E]) =
        listenersOf(request.eventType, context).add(request.listener)

      RegisterEventListenerResponse(addListener(request))
  }

  override val removeEventListener = Service { (request: RemoveEventListenerRequest[_ <: Event], context: EventContext) =>
    logger trace s"Remove listeners: ${request.listener}"

    def removeListener[E <: Event](request: RemoveEventListenerRequest[E]) =
      listenersOf(request.eventType, context).remove(request.listener)

    RemoveEventListenerResponse(removeListener(request))
  }
}
