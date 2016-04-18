package com.talkie.client.app.activities.login

import com.talkie.client.app.activities.common.Controller
import com.talkie.client.domain.services.facebook.{ FacebookServicesComponent, FacebookMessages }
import com.talkie.client.views.common.RichActivity
import com.talkie.client.views.login.LoginViews
import com.talkie.client.views.R
import FacebookMessages.{ ConfigureLoginRequest, ProcessActivityResultRequest }

trait LoginController extends Controller {
  self: RichActivity with FacebookServicesComponent with LoginViews =>

  implicit val c = context
  implicit val ec = context.executionContext

  onCreate {
    setContentView(R.layout.activity_login)
  }

  onPostCreate {
    asyncAction {
      facebookServices.configureLogin(ConfigureLoginRequest(loginButton))
    }
  }

  onActivityResult { (requestCode, resultCode, data) =>
    asyncAction {
      val result = facebookServices.processActivityResult(ProcessActivityResultRequest(requestCode, resultCode, data))
      if (result.handled) logger trace "ActivityResult handled successfully"
      else logger error "ActivityResult not handled"
    }
  }
}
