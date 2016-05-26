package com.talkie.client.core.services

import android.os.Build.VERSION_CODES.LOLLIPOP
import com.talkie.client.core.events.{ NotifyEventListeners, GetEventListeners, EventService, Event }
import org.robolectric.annotation.Config
import org.scalatest.{ RobolectricSuite, Matchers, FlatSpec }

import scalaz.concurrent.Task
import scalaz.{ ~> }

@Config(sdk = Array(LOLLIPOP))
class ServiceSpec
    extends FlatSpec
    with Matchers
    with RobolectricSuite {

  "Service" should "be composable for events" in {
    import com.talkie.client.core.events.EventService._

    val event = new TestEvent

    val result = for {
      listeners <- getListeners[TestEvent]
      result <- notifyEventListeners(event)
    } yield "test"

    val folded = result.foldMap(new (EventService ~> Task) {
      override def apply[A](fa: EventService[A]): Task[A] = fa match {

        case GetEventListeners() =>
          println("getting listeners")
          Task(Set().asInstanceOf[A])

        case NotifyEventListeners(event) =>
          println(s"notify about $event")
          Task(true.asInstanceOf[A])
      }
    }).attemptRun

    println(folded)

    1 shouldBe 1
  }

  class TestEvent extends Event {

    override type Details = String

    override def getDetails: Details = "test"
  }
}
