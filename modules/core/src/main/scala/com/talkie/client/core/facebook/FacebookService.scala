package com.talkie.client.core.facebook

import com.facebook.login.widget.LoginButton
import com.talkie.client.core.services.Service

import scalaz.Free

sealed trait FacebookService[R] extends Service[R]
case object CheckIfLoggedToFacebook extends FacebookService[Boolean]
final case class ConfigureLogin(loginButtonOpt: Option[LoginButton]) extends FacebookService[Unit]
case object LogOutFromFacebook extends FacebookService[Unit]

object FacebookService {

  def checkIfLoggedToFacebook: Free[FacebookService, Boolean] =
    Free.liftF(CheckIfLoggedToFacebook: FacebookService[Boolean])

  def configureLogin(loginButtonOpt: Option[LoginButton]): Free[FacebookService, Unit] =
    Free.liftF(ConfigureLogin(loginButtonOpt): FacebookService[Unit])

  def logOutFromFacebook: Free[FacebookService, Unit] =
    Free.liftF(LogOutFromFacebook: FacebookService[Unit])
}
