package com.talkie.client.core.events

import scalaz.~>
import scalaz.concurrent.Task

final class EventServiceTaskInterpreter(implicit eventActions: EventActions) extends (EventService ~> Task) {

  import EventService._

  override def apply[R](in: EventService[R]): Task[R] = in match {

    case request @ GetEventListeners() => Task {
      eventActions.getListeners(request.eventType).asInstanceOf[R]
    }

    case request @ NotifyEventListeners(event) => Task {
      eventActions.notifyEventListeners(event)(request.eventType).asInstanceOf[R]
    }

    case request @ RegisterEventListener(listener) => Task {
      eventActions.registerEventListener(listener)(request.eventType).asInstanceOf[R]
    }

    case request @ RemoveEventListener(listener) => Task {
      eventActions.removeEventListener(listener)(request.eventType).asInstanceOf[R]
    }
  }
}
