package com.talkie.client.domain.facebook

object FacebookMessages {

  case class CheckLoggedStatusRequest()
  case class CheckLoggedStatusResponse(isLogged: Boolean)
}
