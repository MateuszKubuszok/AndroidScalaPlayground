package com.talkie.client.app.navigation

import android.app.Activity
import android.content.Intent
import com.talkie.client.app.activities.login.LoginActivity
import com.talkie.client.app.activities.discovering.DiscoveringActivity
import com.talkie.client.app.activities.settings.SettingsActivity

trait ManualNavigation {
  self: Activity =>

  protected def loginActivity() = new Intent(this, classOf[LoginActivity])

  protected def discoveringActivity() = new Intent(this, classOf[DiscoveringActivity])
  protected def startDiscoveringActivity() = startActivity(discoveringActivity())

  protected def settingsActivity() = new Intent(this, classOf[SettingsActivity])
  protected def startSettingsActivity() = startActivity(settingsActivity())
}
