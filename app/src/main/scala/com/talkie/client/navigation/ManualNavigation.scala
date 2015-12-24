package com.talkie.client.navigation

import android.app.Activity
import android.content.Intent
import com.talkie.client.activities.login.LoginActivity
import com.talkie.client.activities.main.MainActivity
import com.talkie.client.activities.settings.SettingsActivity

trait ManualNavigation {
  self: Activity =>

  protected def loginActivity() = new Intent(this, classOf[LoginActivity])

  protected def mainActivity() = new Intent(this, classOf[MainActivity])
  protected def startMainActivity() = startActivity(mainActivity())

  protected def settingsActivity() = new Intent(this, classOf[SettingsActivity])
  protected def startSettingsActivity() = startActivity(settingsActivity())
}
