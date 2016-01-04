package com.talkie.client.app.navigation

import android.app.Activity
import android.content.Intent
import com.talkie.client.app.activities.login.LoginActivity
import com.talkie.client.app.activities.main.MainActivity
import com.talkie.client.app.activities.settings.SettingsActivity

trait ManualNavigation {
  self: Activity =>

  protected def loginActivity() = new Intent(this, classOf[LoginActivity])

  protected def mainActivity() = new Intent(this, classOf[MainActivity])
  protected def startMainActivity() = startActivity(mainActivity())

  protected def settingsActivity() = new Intent(this, classOf[SettingsActivity])
  protected def startSettingsActivity() = startActivity(settingsActivity())
}
