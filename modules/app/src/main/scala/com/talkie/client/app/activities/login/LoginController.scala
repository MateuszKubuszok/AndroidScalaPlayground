package com.talkie.client.app.activities.login

import com.talkie.client.{ TR, R }
import com.talkie.client.app.activities.common.{ RichActivity, Controller }
import com.talkie.client.domain.services.facebook.{ FacebookServicesComponent, FacebookMessages }
import FacebookMessages.{ ConfigureLoginRequest, ProcessActivityResultRequest }

trait LoginController extends Controller {
  self: RichActivity with FacebookServicesComponent with LoginViews =>

  implicit val c = context
  implicit val ec = context.executionContext

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
