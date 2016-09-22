package com.talkie.client.app.activities.common

import android.support.v7.app.AppCompatActivity
import com.talkie.client.app.services.ServiceTaskInterpreter
import com.talkie.client.common.components.ActivityImpl
import com.talkie.client.views.settings

trait BaseActivity extends ActivityImpl with ServiceTaskInterpreter { self: Controller =>

  onCreate { _ =>
    initializeController(context.androidContext).foldMap(effInterpreter).unsafePerformSync
  }

  onStart {
    moveToLoginActivityIfLoggedOut.foldMap(effInterpreter).unsafePerformSync
  }

  onResume {
    moveToLoginActivityIfLoggedOut.foldMap(effInterpreter).unsafePerformSync
  }
}

trait AppActivity extends AppCompatActivity with BaseActivity { self: Controller => }

trait AppSettingsActivity extends settings.AppSettingsActivity with BaseActivity { self: Controller => }
