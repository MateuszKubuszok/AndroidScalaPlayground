package com.talkie.client.domain.events

import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.talkie.client.core.events.Event

object FacebookEvents {

  case class LoginSucceeded(result: LoginResult) extends Event {

    override type Details = LoginResult

    override def getDetails: Details = result
  }

  case class LoginCancelled() extends Event {

    override type Details = Unit

    override def getDetails: Details = ()
  }

  case class LoginFailed(error: FacebookException) extends Event {

    override type Details = FacebookException

    override def getDetails: Details = error
  }

  case class LoggedOut() extends Event {

    override type Details = Unit

    override def getDetails: Details = ()
  }

  case class TokenUpdated() extends Event {

    override type Details = Unit

    override def getDetails: Details = ()
  }
}
