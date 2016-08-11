package com.talkie.client.views.login

import scalaz.~>
import scalaz.concurrent.Task

final class LoginViewsServiceTaskInterpreter(
    implicit
    loginViewsActions: LoginViewsActions
) extends (LoginViewsService ~> Task) {

  import LoginViewsService._

  override def apply[T](in: LoginViewsService[T]): Task[T] = in match {

    case InitializeLayout => Task {
      loginViewsActions.initializeLayout().asInstanceOf[T]
    }

    case GetLoginButton => Task {
      loginViewsActions.getLoginButton.asInstanceOf[T]
    }
  }
}
