package com.talkie.client.core.events

import com.talkie.client.common.context.Context
import com.talkie.client.common.events.{ Event, EventListener }

import scala.reflect.ClassTag
import scala.util.Try

trait EventActions {

  def getListeners[E <: Event](eventType: ClassTag[E]): Set[EventListener[E]]

  def notifyEventListeners[E <: Event](event: E)(implicit eventType: ClassTag[E]): Boolean

  def registerEventListener[E <: Event](listener: EventListener[E])(implicit eventType: ClassTag[E]): Boolean

  def removeEventListener[E <: Event](listener: EventListener[E])(implicit eventType: ClassTag[E]): Boolean
}

final class EventActionsImpl(implicit context: Context) extends EventActions {

  private val logger = context.loggerFor(this)

  def getListeners[E <: Event](eventType: ClassTag[E]): Set[EventListener[E]] = context.withLocalState {
    _.eventListeners.getOrElse(eventType, Set()).asInstanceOf[Set[EventListener[E]]]
  }

  def notifyEventListeners[E <: Event](event: E)(implicit eventType: ClassTag[E]): Boolean = {
    logger trace s"Notify listeners about event: $event"
    (for {
      listener <- getListeners(eventType).toSeq
      _ = logger trace s"Notifying $listener about $event"
    } yield Try(listener.handleEvent(event)).isSuccess) forall identity
  }

  def registerEventListener[E <: Event](listener: EventListener[E])(implicit eventType: ClassTag[E]): Boolean =
    context.withLocalStateUpdated { state =>
      logger trace s"Register listeners: $listener"
      val oldListeners = state.eventListeners.getOrElse(eventType, Set())
      val newListeners = oldListeners + listener
      val newState = state.copy(eventListeners = state.eventListeners + (eventType -> newListeners))
      newState -> (newListeners.size != oldListeners.size)
    }

  def removeEventListener[E <: Event](listener: EventListener[E])(implicit eventType: ClassTag[E]): Boolean =
    context.withLocalStateUpdated { state =>
      logger trace s"Remove listeners: $listener"
      val oldListeners = state.eventListeners.getOrElse(eventType, Set())
      val newListeners = oldListeners - listener
      val newState = state.copy(eventListeners = state.eventListeners + (eventType -> newListeners))
      newState -> (newListeners.size != oldListeners.size)
    }
}
