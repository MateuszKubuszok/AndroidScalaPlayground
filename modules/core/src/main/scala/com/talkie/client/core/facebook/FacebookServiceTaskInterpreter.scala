package com.talkie.client.core.facebook

import scalaz.~>
import scalaz.concurrent.Task

final class FacebookServiceTaskInterpreter(
    implicit
    facebookActions: FacebookActions
) extends (FacebookService ~> Task) {

  import FacebookService._

  override def apply[T](in: FacebookService[T]): Task[T] = in match {

    case CheckIfLoggedToFacebook => Task {
      facebookActions.checkIfLogged().asInstanceOf[T]
    }

    case ConfigureLogin(requestor, loginButtonOpt) => Task {
      facebookActions.configureLogin(requestor, loginButtonOpt).asInstanceOf[T]
    }

    case LogOutFromFacebook => Task {
      facebookActions.logout().asInstanceOf[T]
    }
  }
}
