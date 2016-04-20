package com.talkie.client.app.navigation

import android.content.Intent
import com.talkie.client.app.activities.login.LoginActivity
import com.talkie.client.app.activities.discovering.DiscoveringActivity
import com.talkie.client.app.activities.settings.SettingsActivity
import com.talkie.client.views.common.RichActivity
import org.scaloid.common._

trait ManualNavigation {

  def loginActivity(): Intent
  def startLoginActivity(): Unit

  def discoveringActivity(): Intent
  def startDiscoveringActivity(): Unit

  def settingsActivity(): Intent
  def startSettingsActivity(): Unit
}

class ManualNavigationImpl(owner: RichActivity) extends ManualNavigation {

  private implicit val ac: android.content.Context = owner.ctx

  def loginActivity() = SIntent[LoginActivity]
  def startLoginActivity() = owner.startActivity[LoginActivity]

  def discoveringActivity() = SIntent[DiscoveringActivity]
  def startDiscoveringActivity() = owner.startActivity[DiscoveringActivity]

  def settingsActivity() = SIntent[SettingsActivity]
  def startSettingsActivity() = owner.startActivity[SettingsActivity]
}
