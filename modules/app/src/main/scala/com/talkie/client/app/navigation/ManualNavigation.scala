package com.talkie.client.app.navigation

import com.talkie.client.app.activities.common.RichActivity
import com.talkie.client.app.activities.login.LoginActivity
import com.talkie.client.app.activities.discovering.DiscoveringActivity
import com.talkie.client.app.activities.settings.SettingsActivity
import org.scaloid.common._

trait ManualNavigation {
  self: RichActivity =>

  implicit val aContext: android.content.Context = ctx

  protected def loginActivity() = SIntent[LoginActivity]

  protected def discoveringActivity() = SIntent[DiscoveringActivity]
  protected def startDiscoveringActivity() = startService[DiscoveringActivity]

  protected def settingsActivity() = SIntent[SettingsActivity]
  protected def startSettingsActivity() = startService[SettingsActivity]
}
