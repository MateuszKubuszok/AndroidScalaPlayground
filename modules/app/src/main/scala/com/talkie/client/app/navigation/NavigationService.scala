package com.talkie.client.app.navigation

import scalaz.{ :<:, Free }

sealed trait NavigationService[R]

object NavigationService {

  case object ConfigureNavigation extends NavigationService[Unit]
  sealed trait MoveTo[R] extends NavigationService[R]
  case object MoveToDiscovering extends MoveTo[Unit]
  case object MoveToSettings extends MoveTo[Unit]
  case object MoveToLogin extends NavigationService[Unit]
  case class MoveToLoginOrElse[R](moveTo: MoveTo[R]) extends NavigationService[Unit]

  object moveTo {

    def discovering: MoveTo[Unit] = MoveToDiscovering
    def settings: MoveTo[Unit] = MoveToSettings
  }

  class Ops[S[_]](implicit s0: NavigationService :<: S) {

    def configureNavigation: Free[S, Unit] =
      Free.liftF(s0.inj(ConfigureNavigation))

    def moveToDiscovering: Free[S, Unit] =
      Free.liftF(s0.inj(MoveToDiscovering))

    def moveToSettings: Free[S, Unit] =
      Free.liftF(s0.inj(MoveToSettings))

    def moveToLogin: Free[S, Unit] =
      Free.liftF(s0.inj(MoveToLogin))

    def moveToLoginOrElse[R](moveTo: MoveTo[R]): Free[S, Unit] =
      Free.liftF(s0.inj(MoveToLoginOrElse(moveTo)))
  }
}
