package com.talkie.client.domain.repositories.profiles

import com.talkie.client.core.repositories.Entity

case class FacebookId(value: Long)

case class ProfileData(
  facebookId: FacebookId
)

trait Profile extends Entity[Profile] {

  type Data = ProfileData
}
