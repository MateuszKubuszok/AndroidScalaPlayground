package com.talkie.client.views.login

import com.talkie.client.common.components.Activity
import com.talkie.client.common.context.Context
import com.talkie.client.common.services.{ ~@~>, ~&~> }

import scalaz.concurrent.Task

trait LoginViewsServiceInterpreter extends (LoginViewsService ~@~> Task)

object LoginViewsServiceInterpreter extends (LoginViewsService ~&~> Task)

final class LoginViewsServiceInterpreterImpl(
    context:  Context,
    activity: Activity
) extends LoginViewsServiceInterpreter {

  private val actions = new LoginViewsActions(context, activity, new LoginViewsImpl(activity))

  override def apply[T](in: LoginViewsService[T]): Task[T] = in match {

    case InitializeLayout => Task {
      actions.initializeLayout().asInstanceOf[T]
    }

    case GetLoginButton => Task {
      actions.getLoginButton.asInstanceOf[T]
    }
  }
}
