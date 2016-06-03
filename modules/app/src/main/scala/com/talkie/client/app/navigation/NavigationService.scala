package com.talkie.client.app.navigation

import com.talkie.client.core.services.{ Service => GenericService }

import scalaz.Free

sealed trait NavigationService[R] extends GenericService[R]
case object ConfigureNavigation extends NavigationService[Unit]
sealed trait MoveTo[R] extends NavigationService[R]
case object MoveToDiscovering extends MoveTo[Unit]
case object MoveToSettings extends MoveTo[Unit]
case object MoveToLogin extends NavigationService[Unit]
case class MoveToLoginOrElse[R](moveTo: MoveTo[R]) extends NavigationService[Unit]

trait NavigationServiceFrees[S[R] >: NavigationService[R]] {

  def configureNavigation: Free[S, Unit] =
    Free.liftF(ConfigureNavigation: S[Unit])

  def moveToDiscovering: Free[S, Unit] =
    Free.liftF(MoveToDiscovering: S[Unit])

  def moveToSettings: Free[S, Unit] =
    Free.liftF(MoveToSettings: S[Unit])

  def moveToLogin: Free[S, Unit] =
    Free.liftF(MoveToLogin: S[Unit])

  def moveToLoginOrElse[R](moveTo: MoveTo[R]): Free[S, Unit] =
    Free.liftF(MoveToLoginOrElse(moveTo): S[Unit])

  object moveTo {

    def discovering: MoveTo[Unit] = MoveToDiscovering
    def settings: MoveTo[Unit] = MoveToSettings
  }
}

object NavigationService extends NavigationServiceFrees[NavigationService]
object Service extends NavigationServiceFrees[GenericService]
