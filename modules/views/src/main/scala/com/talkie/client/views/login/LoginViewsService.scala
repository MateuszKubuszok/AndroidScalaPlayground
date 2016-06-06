package com.talkie.client.views.login

import com.facebook.login.widget.LoginButton
import com.talkie.client.common.services.{ Service => GenericService }

import scalaz.Free

sealed trait LoginViewsService[R] extends GenericService[R]
case object InitializeLayout extends LoginViewsService[Unit]
case object GetLoginButton extends LoginViewsService[Option[LoginButton]]

trait LoginViewsServiceFrees[S[R] >: LoginViewsService[R]] {

  def initializeLayout: Free[S, Unit] =
    Free.liftF(InitializeLayout: S[Unit])

  def getLoginButton: Free[S, Option[LoginButton]] =
    Free.liftF(GetLoginButton: S[Option[LoginButton]])
}

object LoginViewsService extends LoginViewsServiceFrees[LoginViewsService]
object Service extends LoginViewsServiceFrees[GenericService]
