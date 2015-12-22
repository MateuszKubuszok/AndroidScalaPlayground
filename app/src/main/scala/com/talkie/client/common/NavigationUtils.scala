package com.talkie.client.common

import android.app.Activity
import android.content.Intent
import com.talkie.client.{LoginActivity, MainActivity}

trait NavigationUtils extends Activity {

  protected def loginActivity() = new Intent(this, classOf[LoginActivity])

  protected def mainActivity() = new Intent(this, classOf[MainActivity])
  protected def startMainActivity() = startActivity(mainActivity())
}
