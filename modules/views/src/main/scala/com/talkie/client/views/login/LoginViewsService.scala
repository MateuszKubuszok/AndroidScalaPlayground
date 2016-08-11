package com.talkie.client.views.login

import com.facebook.login.widget.LoginButton

import scalaz.{ :<:, Free }

sealed trait LoginViewsService[R]

object LoginViewsService {

  case object InitializeLayout extends LoginViewsService[Unit]
  case object GetLoginButton extends LoginViewsService[Option[LoginButton]]

  class Ops[S[_]](implicit s0: LoginViewsService :<: S) {

    def initializeLayout: Free[S, Unit] =
      Free.liftF(s0.inj(InitializeLayout))

    def getLoginButton: Free[S, Option[LoginButton]] =
      Free.liftF(s0.inj(GetLoginButton))
  }
}
