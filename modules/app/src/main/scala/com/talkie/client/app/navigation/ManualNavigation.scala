package com.talkie.client.app.navigation

import com.talkie.client.app.activities.login.LoginActivity
import com.talkie.client.app.activities.discovering.DiscoveringActivity
import com.talkie.client.app.activities.settings.SettingsActivity
import com.talkie.client.views.common.RichActivity
import org.scaloid.common._

trait ManualNavigation {
  self: RichActivity =>

  private implicit val context: android.content.Context = ctx

  protected def loginActivity() = SIntent[LoginActivity]

  protected def discoveringActivity() = SIntent[DiscoveringActivity]
  protected def startDiscoveringActivity() = startActivity[DiscoveringActivity]

  protected def settingsActivity() = SIntent[SettingsActivity]
  protected def startSettingsActivity() = startActivity[SettingsActivity]
}
