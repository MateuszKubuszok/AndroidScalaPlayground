package com.talkie.client.core.events

import com.talkie.client.core.events.EventMessages._
import com.talkie.client.core.logging.LoggerComponent
import com.talkie.client.core.services.{ AsyncService, Service, SyncService }

import scala.collection.mutable
import scala.util.Try

trait EventBus {

  def getListeners: SyncService[GetListenersRequest, GetListenersResponse]
  def notifyEventListeners: AsyncService[NotifyEventListenersRequest, NotifyEventListenersResponse]
  def registerEventListener: SyncService[RegisterEventListenerRequest[_ <: Event], RegisterEventListenerResponse]
  def removeEventListener: SyncService[RemoveEventListenerRequest[_ <: Event], RemoveEventListenerResponse]
}

trait EventBusComponent {

  def eventBus: EventBus
}

private[events] object EventBusComponentImpl {

  private val listeners = mutable.Map[Event, mutable.Set[EventListener[_]]]()
}

trait EventBusComponentImpl extends EventBusComponent {
  self: LoggerComponent =>

  protected def listenersOf[E <: Event](event: E) = {
    import EventBusComponentImpl.listeners
    listeners.getOrElseUpdate(event, mutable.Set()).asInstanceOf[mutable.Set[EventListener[E]]]
  }

  object eventBus extends EventBus {

    override val getListeners = Service { request: GetListenersRequest =>
      GetListenersResponse(listenersOf(request.event).toSet)
    }

    override val notifyEventListeners = Service.async { request: NotifyEventListenersRequest =>
      logger trace s"Notify listeners about event: ${request.event}"

      val results = for {
        listener <- listenersOf(request.event)
      } yield Try(listener.handleEvent(request.event)).isSuccess

      NotifyEventListenersResponse(results forall identity)
    }

    override val registerEventListener = Service { request: RegisterEventListenerRequest[_ <: Event] =>
      def addListener[E <: Event](request: RegisterEventListenerRequest[E]) =
        listenersOf(request.event).add(request.listener)
      RegisterEventListenerResponse(addListener(request))
    }

    override val removeEventListener = Service { request: RemoveEventListenerRequest[_ <: Event] =>
      def removeListener[E <: Event](request: RemoveEventListenerRequest[E]) =
        listenersOf(request.event).remove(request.listener)
      RemoveEventListenerResponse(removeListener(request))
    }
  }
}