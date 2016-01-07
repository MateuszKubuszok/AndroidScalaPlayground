package com.talkie.client.app.activities.login

import com.talkie.client.{ TR, R }
import com.talkie.client.app.activities.common.{ RichActivity, Controller }
import com.talkie.client.domain.services.facebook.{ FacebookServicesComponent, FacebookMessages }
import FacebookMessages.{ ConfigureLoginRequest, ProcessActivityResultRequest }

trait LoginController extends Controller {
  self: RichActivity with FacebookServicesComponent with LoginViews =>

  implicit val c = context
  implicit val ec = context.executionContext

  onCreate {
    //    setContentView(R.layout.activity_login)
    contentView = loginLayout
  }

  onPostCreate {
    asyncAction {
      //      facebookServices.configureLogin(ConfigureLoginRequest(findView(TR.login_button)))
      facebookServices.configureLogin(ConfigureLoginRequest(loginButton))
      logger trace s"layout => ${loginLayout.getChildCount}"
      logger trace s"loginButton => ${loginButton.getParent}"
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
