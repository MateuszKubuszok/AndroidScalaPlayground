package com.talkie.client.core.facebook

import com.facebook.login.widget.LoginButton
import com.talkie.client.common.components.Activity

import scalaz.{ :<:, Free }

sealed trait FacebookService[R]

object FacebookService {

  case object CheckIfLoggedToFacebook extends FacebookService[Boolean]
  final case class ConfigureLogin(requestor: Activity, loginButtonOpt: () => Option[LoginButton]) extends FacebookService[Unit]
  case object LogOutFromFacebook extends FacebookService[Unit]

  class Ops[S[_]](implicit s0: FacebookService :<: S) {

    def checkIfLoggedToFacebook: Free[S, Boolean] =
      Free.liftF(s0.inj(CheckIfLoggedToFacebook))

    def configureLogin(requestor: Activity, loginButtonOpt: () => Option[LoginButton]): Free[S, Unit] =
      Free.liftF(s0.inj(ConfigureLogin(requestor, loginButtonOpt)))

    def logOutFromFacebook: Free[S, Unit] =
      Free.liftF(s0.inj(LogOutFromFacebook))
  }
}

