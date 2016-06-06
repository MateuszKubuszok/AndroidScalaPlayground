package com.talkie.client.app.activities.common

import android.support.v7.app.AppCompatActivity
import com.talkie.client.app.services.ServiceInterpreterImpl
import com.talkie.client.common.components.ActivityImpl
import com.talkie.client.common.services.Service
import com.talkie.client.common.services.ServiceInterpreter._
import com.talkie.client.views.settings

import scalaz.concurrent.Task

private[common] trait BaseActivity extends ActivityImpl { self: Controller =>

  protected val viewInterpreter: PartialFunction[Service[Nothing], Task[Nothing]]

  override protected implicit lazy val serviceInterpreter = new ServiceInterpreterImpl(context, this, viewInterpreter)

  onCreate { _ =>
    initializeController(context.androidContext).fireAndWait()
  }

  onStart {
    moveToLoginActivityIfLoggedOut.fireAndWait()
  }

  onResume {
    moveToLoginActivityIfLoggedOut.fireAndWait()
  }
}

trait AppActivity extends AppCompatActivity with BaseActivity { self: Controller => }

trait AppSettingsActivity extends settings.AppSettingsActivity with BaseActivity { self: Controller =>

  override protected val viewInterpreter = PartialFunction.empty[Service[Nothing], Task[Nothing]]
}
