package com.talkie.client.core.events

import com.talkie.client.common.context.Context
import com.talkie.client.common.events.{ Event, EventListener }

import scala.reflect.ClassTag
import scala.util.Try

private[events] final class EventActions(context: Context) {

  private val logger = context.loggerFor(this)

  def listenersOf[E <: Event](eventType: ClassTag[E]): Set[EventListener[E]] = context.withSharedState {
    _.eventListeners.getOrElse(eventType, Set()).asInstanceOf[Set[EventListener[E]]]
  }

  def notifyFor[E <: Event](typedRequest: NotifyEventListeners[E]): Boolean = {
    logger trace s"Notify listeners about event: $typedRequest.event"
    (for {
      listener <- listenersOf(typedRequest.eventType).toSeq
      _ = logger trace s"Notifying $listener about ${typedRequest.event}"
    } yield Try(listener.handleEvent(typedRequest.event)).isSuccess) forall identity
  }

  def addListener[E <: Event](typedRequest: RegisterEventListener[E]): Boolean =
    context.withSharedStateUpdated { state =>
      logger trace s"Register listeners: $typedRequest.listener"
      val oldListeners = state.eventListeners.getOrElse(typedRequest.eventType, Set())
      val newListeners = oldListeners + typedRequest.listener
      val newState = state.copy(eventListeners = state.eventListeners + (typedRequest.eventType -> newListeners))
      newState -> (newListeners.size != oldListeners.size)
    }

  def removeListener[E <: Event](typedRequest: RemoveEventListener[E]): Boolean =
    context.withSharedStateUpdated { state =>
      logger trace s"Remove listeners: $typedRequest.listener"
      val oldListeners = state.eventListeners.getOrElse(typedRequest.eventType, Set())
      val newListeners = oldListeners - typedRequest.listener
      val newState = state.copy(eventListeners = state.eventListeners + (typedRequest.eventType -> newListeners))
      newState -> (newListeners.size != oldListeners.size)
    }
}
