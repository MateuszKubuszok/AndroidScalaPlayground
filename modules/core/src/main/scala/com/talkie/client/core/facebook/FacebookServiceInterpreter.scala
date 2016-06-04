package com.talkie.client.core.facebook

import com.talkie.client.common.components.Activity
import com.talkie.client.common.context.Context
import com.talkie.client.common.services.{ ~@~>, ~&~> }
import com.talkie.client.core.events.EventServiceInterpreter

import scalaz.concurrent.Task

trait FacebookServiceInterpreter extends (FacebookService ~@~> Task)

object FacebookServiceInterpreter extends (FacebookService ~&~> Task)

final class FacebookServiceInterpreterImpl(
    context:  Context,
    activity: Activity,
    eventSI:  EventServiceInterpreter
) extends FacebookServiceInterpreter {

  private val actions = new FacebookActions(context, activity, eventSI)

  override def apply[T](in: FacebookService[T]): Task[T] = in match {

    case CheckIfLoggedToFacebook => Task {
      actions.checkIfLogged().asInstanceOf[T]
    }

    case ConfigureLogin(loginButtonOpt) => Task {
      actions.configureLogin(loginButtonOpt).asInstanceOf[T]
    }

    case LogOutFromFacebook => Task {
      actions.logout().asInstanceOf[T]
    }
  }
}
