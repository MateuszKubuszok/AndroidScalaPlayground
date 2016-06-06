package com.talkie.client.app.activities.login

import com.talkie.client.app.activities.common.AppActivity
import com.talkie.client.common.services.ServiceInterpreter._
import com.talkie.client.views.login.LoginViewsServiceInterpreterImpl

final class LoginActivity extends AppActivity with LoginController {

  override protected val viewInterpreter = new LoginViewsServiceInterpreterImpl(context, this).forService

  override def loginButtonOpt() = getLoginButton.fireAndWait().getOrElse(None)

  onCreate { _ =>
    initializeLayout.fireAndWait()
  }

  onStart {
    moveToDiscoveringActivityIfLoggedIn.fireAndForget()
  }

  onResume {
    moveToDiscoveringActivityIfLoggedIn.fireAndForget()
  }
}
