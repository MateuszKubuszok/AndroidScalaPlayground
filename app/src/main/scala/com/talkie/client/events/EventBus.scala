package com.talkie.client.events

import com.talkie.client.events.EventMessages._
import com.talkie.client.services.{Service, AsyncService}

import scala.collection.mutable
import scala.util.Try

trait EventBus {

  def notifyEventListeners: AsyncService[NotifyEventListenersRequest, NotifyEventListenersResponse]
  def registerEventListener: AsyncService[RegisterEventListenerRequest[_ <: Event], RegisterEventListenerResponse]
  def removeEventListener: AsyncService[RemoveEventListenerRequest[_ <: Event], RemoveEventListenerResponse]
}

trait EventBusComponent {

  def eventBus: EventBus
}

private[events] object EventBusComponentImpl {

  private val listeners = mutable.Map[Event, mutable.Set[EventListener[_]]]()
}

trait EventBusComponentImpl extends EventBusComponent {

  protected def listenersOf[E <: Event](event: E) = {
    import EventBusComponentImpl.listeners
    listeners.getOrElseUpdate(event, mutable.Set()).asInstanceOf[mutable.Set[EventListener[E]]]
  }

  object eventBus extends EventBus {

    val notifyEventListeners = Service.async { request: NotifyEventListenersRequest =>

      val results = for {
        listener <- listenersOf(request.event)
      } yield Try(listener.handleEvent(request.event))

      NotifyEventListenersResponse(results.forall(_.isSuccess))
    }

    val registerEventListener = Service.async { request: RegisterEventListenerRequest[_ <: Event] =>
      def addListener[E <: Event](request: RegisterEventListenerRequest[E]) =
        listenersOf(request.event).add(request.listener)
      RegisterEventListenerResponse(addListener(request))
    }

    val removeEventListener = Service.async { request: RemoveEventListenerRequest[_ <: Event] =>
      def removeListener[E <: Event](request: RemoveEventListenerRequest[E]) =
        listenersOf(request.event).add(request.listener)
      RemoveEventListenerResponse(removeListener(request))
    }
  }
}