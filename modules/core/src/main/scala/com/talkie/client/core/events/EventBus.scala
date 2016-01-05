package com.talkie.client.core.events

import com.talkie.client.core.events.EventMessages._
import com.talkie.client.core.logging.LoggerComponent
import com.talkie.client.core.services.{ AsyncService, Service, SyncService }

import scala.collection.mutable
import scala.reflect.ClassTag
import scala.util.Try

trait EventBus {

  def getListeners: SyncService[GetListenersRequest[_ <: Event], GetListenersResponse]
  def notifyEventListeners: AsyncService[NotifyEventListenersRequest[_ <: Event], NotifyEventListenersResponse]
  def registerEventListener: SyncService[RegisterEventListenerRequest[_ <: Event], RegisterEventListenerResponse]
  def removeEventListener: SyncService[RemoveEventListenerRequest[_ <: Event], RemoveEventListenerResponse]
}

trait EventBusComponent {

  def eventBus: EventBus
}

private[events] object EventBusComponentImpl {

  private val listeners = mutable.Map[ClassTag[_ <: Event], mutable.Set[EventListener[_]]]()
}

trait EventBusComponentImpl extends EventBusComponent {
  self: LoggerComponent =>

  protected def listenersOf[E <: Event](eventType: ClassTag[E]) = {
    import EventBusComponentImpl.listeners
    listeners.getOrElseUpdate(eventType, mutable.Set()).asInstanceOf[mutable.Set[EventListener[E]]]
  }

  object eventBus extends EventBus {

    override val getListeners = Service { request: GetListenersRequest[_ <: Event] =>
      GetListenersResponse(listenersOf(request.eventType).toSet)
    }

    override val notifyEventListeners = Service.async { request: NotifyEventListenersRequest[_ <: Event] =>
      logger trace s"Notify listeners about event: ${request.event}"

      def notifyFor[E <: Event](typedRequest: NotifyEventListenersRequest[E]) = for {
        listener <- listenersOf(typedRequest.eventType)
      } yield Try(listener.handleEvent(typedRequest.event)).isSuccess

      val results = notifyFor(request)

      NotifyEventListenersResponse(results forall identity)
    }

    override val registerEventListener = Service { request: RegisterEventListenerRequest[_ <: Event] =>
      def addListener[E <: Event](request: RegisterEventListenerRequest[E]) =
        listenersOf(request.eventType).add(request.listener)
      RegisterEventListenerResponse(addListener(request))
    }

    override val removeEventListener = Service { request: RemoveEventListenerRequest[_ <: Event] =>
      def removeListener[E <: Event](request: RemoveEventListenerRequest[E]) =
        listenersOf(request.eventType).remove(request.listener)
      RemoveEventListenerResponse(removeListener(request))
    }
  }
}
