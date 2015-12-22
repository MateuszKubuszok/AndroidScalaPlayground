package com.talkie.client.facebook

import android.content.Intent
import com.talkie.client.common.TalkieActivity

trait FacebookActivity extends TalkieActivity {

  private val Extra_ActivityAfterLogin = "activity_after_login"

  protected def startLoginActivityThenReturnTo(intent: Intent) = {
    logger trace s"Requested FB login then ${intent.getComponent.getClassName}"
    startActivity(loginActivity().putExtra(Extra_ActivityAfterLogin, intent))
  }
  protected def returnFromLoginActivity() = {
    logger trace s"FB login succeeded, now into ${getIntent.getComponent.getClassName}"
    startActivity(getIntent.getParcelableExtra[Intent](Extra_ActivityAfterLogin))
  }

  protected def isLoggedIn: Boolean = {
    // TODO: implement
    false
  }
}
