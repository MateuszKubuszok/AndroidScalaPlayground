package com.talkie.client.app.navigation

import android.content.Intent
import com.talkie.client.app.activities.login.LoginActivity
import com.talkie.client.app.activities.discovering.DiscoveringActivity
import com.talkie.client.app.activities.settings.SettingsActivity
import com.talkie.client.views.common.RichActivity
import org.scaloid.common._

trait ManualNavigation {

  def loginActivity(): Intent

  def discoveringActivity(): Intent
  def startDiscoveringActivity(): Unit

  def settingsActivity(): Intent
  def startSettingsActivity(): Unit
}

class ManualNavigationImpl(activity: RichActivity) extends ManualNavigation {

  private implicit val ac: android.content.Context = activity.ctx

  def loginActivity() = SIntent[LoginActivity]

  def discoveringActivity() = SIntent[DiscoveringActivity]
  def startDiscoveringActivity() = activity.startActivity[DiscoveringActivity]

  def settingsActivity() = SIntent[SettingsActivity]
  def startSettingsActivity() = activity.startActivity[SettingsActivity]
}
