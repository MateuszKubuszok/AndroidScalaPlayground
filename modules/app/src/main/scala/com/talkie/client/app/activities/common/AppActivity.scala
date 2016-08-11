package com.talkie.client.app.activities.common

import android.support.v7.app.AppCompatActivity
import com.talkie.client.app.navigation.NavigationServiceTaskInterpreter
import com.talkie.client.app.services.ServiceTaskInterpreter
import com.talkie.client.common.services.EnrichNTOps._
import com.talkie.client.common.components.ActivityImpl
import com.talkie.client.core.facebook.FacebookServiceTaskInterpreter
import com.talkie.client.views.settings

trait BaseActivity extends ActivityImpl with ServiceTaskInterpreter { self: Controller =>

  private val i0 = new FacebookServiceTaskInterpreter
  private val i1 = i0 :+: new NavigationServiceTaskInterpreter

  private val interpreter = i1

  onCreate { _ =>
    initializeController(context.androidContext).foldMap(interpreter).unsafePerformSync
  }

  onStart {
    moveToLoginActivityIfLoggedOut.foldMap(interpreter).unsafePerformSync
  }

  onResume {
    moveToLoginActivityIfLoggedOut.foldMap(interpreter).unsafePerformSync
  }
}

trait AppActivity extends AppCompatActivity with BaseActivity { self: Controller => }

trait AppSettingsActivity extends settings.AppSettingsActivity with BaseActivity { self: Controller => }
