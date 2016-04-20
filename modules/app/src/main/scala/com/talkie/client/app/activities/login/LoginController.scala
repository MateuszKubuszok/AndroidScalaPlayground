package com.talkie.client.app.activities.login

import com.talkie.client.app.activities.common.Controller
import com.talkie.client.domain.services.facebook.FacebookMessages
import com.talkie.client.views.common.RichActivity
import com.talkie.client.views.login.LoginViews
import com.talkie.client.views.R
import FacebookMessages.{ ConfigureLoginRequest, ProcessActivityResultRequest }

trait LoginController extends Controller {
  self: RichActivity with LoginViews =>

  implicit val c = context
  implicit val ec = context.serviceExecutionContext

  onCreate {
    setContentView(R.layout.activity_login)
  }

  onPostCreate {
    asyncAction {
      sharedServices { services =>
        services.facebookServices.configureLogin(ConfigureLoginRequest(loginButton))
      }
    }
  }

  onActivityResult { (requestCode, resultCode, data) =>
    asyncAction {
      sharedServices { services =>
        val result = services.facebookServices.processActivityResult(
          ProcessActivityResultRequest(requestCode, resultCode, data)
        )
        if (result.handled) context.loggerFor(this) trace "ActivityResult handled successfully"
        else context.loggerFor(this) error "ActivityResult not handled"
      }
    }
  }
}
