package com.talkie.client.views.discovering

import com.talkie.client.views.R

object MenuOptions extends Enumeration {
  type MenuOption = Value
  val OpenSettings = Value(R.id.action_settings)
  val Logout = Value(R.id.action_logout)
}
