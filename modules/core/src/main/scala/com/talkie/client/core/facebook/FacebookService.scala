package com.talkie.client.core.facebook

import com.facebook.login.widget.LoginButton
import com.talkie.client.core.services.{ Service => GenericService }

import scalaz.Free

sealed trait FacebookService[R] extends GenericService[R]
case object CheckIfLoggedToFacebook extends FacebookService[Boolean]
final case class ConfigureLogin(loginButtonOpt: () => Option[LoginButton]) extends FacebookService[Unit]
case object LogOutFromFacebook extends FacebookService[Unit]

trait FacebookServiceFrees[S[R] >: FacebookService[R]] {

  def checkIfLoggedToFacebook: Free[S, Boolean] =
    Free.liftF(CheckIfLoggedToFacebook: S[Boolean])

  def configureLogin(loginButtonOpt: () => Option[LoginButton]): Free[S, Unit] =
    Free.liftF(ConfigureLogin(loginButtonOpt): S[Unit])

  def logOutFromFacebook: Free[S, Unit] =
    Free.liftF(LogOutFromFacebook: S[Unit])
}

object FacebookService extends FacebookServiceFrees[FacebookService]
object Service extends FacebookServiceFrees[GenericService]
