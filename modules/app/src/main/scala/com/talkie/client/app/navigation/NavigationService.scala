package com.talkie.client.app.navigation

import com.talkie.client.core.services.Service

import scalaz.Free

sealed trait NavigationService[R] extends Service[R]
case object ConfigureNavigation extends NavigationService[Unit]
sealed trait MoveTo[R] extends NavigationService[R]
case object MoveToDiscovering extends MoveTo[Unit]
case object MoveToSettings extends MoveTo[Unit]
case object MoveToLogin extends NavigationService[Unit]
case class MoveToLoginOrElse[R](moveTo: MoveTo[R]) extends NavigationService[Unit]

object NavigationService {

  def configureNavigation: Free[NavigationService, Unit] =
    Free.liftF(ConfigureNavigation: NavigationService[Unit])

  def moveToDiscovering: Free[NavigationService, Unit] =
    Free.liftF(MoveToDiscovering: NavigationService[Unit])

  def moveToSettings: Free[NavigationService, Unit] =
    Free.liftF(MoveToSettings: NavigationService[Unit])

  def moveToLogin: Free[NavigationService, Unit] =
    Free.liftF(MoveToLogin: NavigationService[Unit])

  def moveToLoginOrElse[R](moveTo: MoveTo[R]): Free[NavigationService, Unit] =
    Free.liftF(MoveToLoginOrElse(moveTo): NavigationService[Unit])

  object moveTo {

    def discovering: MoveTo[Unit] = MoveToDiscovering
    def settings: MoveTo[Unit] = MoveToSettings
  }
}
