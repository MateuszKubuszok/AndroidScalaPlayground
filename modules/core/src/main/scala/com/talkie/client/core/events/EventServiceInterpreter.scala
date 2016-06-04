package com.talkie.client.core.events

import com.talkie.client.common.context.Context
import com.talkie.client.common.events.{ Event, EventListener }
import com.talkie.client.common.services.{ ~@~>, ~&~> }

import scala.reflect.ClassTag
import scala.util.Try
import scalaz.concurrent.Task

trait EventServiceInterpreter extends (EventService ~@~> Task)

object EventServiceInterpreter extends (EventService ~&~> Task)

final class EventServiceInterpreterImpl(context: Context) extends EventServiceInterpreter {

  private val logger = context.loggerFor(this)

  override def apply[R](in: EventService[R]): Task[R] = in match {

    case request @ GetEventListeners() => Task {
      listenersOf(request.eventType).asInstanceOf[R]
    }

    case request @ NotifyEventListeners(event) => Task {
      logger trace s"Notify listeners about event: $event"
      notifyFor(request).asInstanceOf[R]
    }

    case request @ RegisterEventListener(listener) => Task {
      logger trace s"Register listeners: $listener"
      addListener(request).asInstanceOf[R]
    }

    case request @ RemoveEventListener(listener) => Task {
      logger trace s"Remove listeners: $listener"
      removeListener(request).asInstanceOf[R]
    }
  }

  private def listenersOf[E <: Event](eventType: ClassTag[E]): Set[EventListener[E]] = context.withSharedState {
    _.eventListeners.get(eventType).getOrElse(Set()).asInstanceOf[Set[EventListener[E]]]
  }

  private def notifyFor[E <: Event](typedRequest: NotifyEventListeners[E]): Boolean = (for {
    listener <- listenersOf(typedRequest.eventType).toSeq
    _ = logger trace s"Notifying $listener about ${typedRequest.event}"
  } yield Try(listener.handleEvent(typedRequest.event)).isSuccess) forall identity

  private def addListener[E <: Event](typedRequest: RegisterEventListener[E]): Boolean =
    context.withSharedStateUpdated { state =>
      val oldListeners = state.eventListeners.getOrElse(typedRequest.eventType, Set())
      val newListeners = oldListeners + typedRequest.listener
      val newState = state.copy(eventListeners = state.eventListeners + (typedRequest.eventType -> newListeners))
      newState -> (newListeners.size != oldListeners.size)
    }

  private def removeListener[E <: Event](typedRequest: RemoveEventListener[E]): Boolean =
    context.withSharedStateUpdated { state =>
      val oldListeners = state.eventListeners.getOrElse(typedRequest.eventType, Set())
      val newListeners = oldListeners - typedRequest.listener
      val newState = state.copy(eventListeners = state.eventListeners + (typedRequest.eventType -> newListeners))
      newState -> (newListeners.size != oldListeners.size)
    }
}
