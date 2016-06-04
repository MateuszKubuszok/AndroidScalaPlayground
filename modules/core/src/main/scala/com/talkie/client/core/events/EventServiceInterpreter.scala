package com.talkie.client.core.events

import com.talkie.client.common.context.Context
import com.talkie.client.common.services.{ ~@~>, ~&~> }

import scalaz.concurrent.Task

trait EventServiceInterpreter extends (EventService ~@~> Task)

object EventServiceInterpreter extends (EventService ~&~> Task)

final class EventServiceInterpreterImpl(context: Context) extends EventServiceInterpreter {

  private val actions = new EventActions(context)

  override def apply[R](in: EventService[R]): Task[R] = in match {

    case request @ GetEventListeners() => Task {
      actions.listenersOf(request.eventType).asInstanceOf[R]
    }

    case request @ NotifyEventListeners(event) => Task {
      actions.notifyFor(request).asInstanceOf[R]
    }

    case request @ RegisterEventListener(listener) => Task {
      actions.addListener(request).asInstanceOf[R]
    }

    case request @ RemoveEventListener(listener) => Task {
      actions.removeListener(request).asInstanceOf[R]
    }
  }
}
