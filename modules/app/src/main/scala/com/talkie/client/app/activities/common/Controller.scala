package com.talkie.client.app.activities.common

import com.facebook.login.widget.LoginButton
import com.talkie.client.app.navigation.NavigationService.configureNavigation
import com.talkie.client.core.components.Activity
import com.talkie.client.core.context.Context
import com.talkie.client.core.facebook.FacebookService.configureLogin
import com.talkie.client.core.services.ServiceInterpreter
import com.talkie.client.core.services.ServiceInterpreter.GeneralizeService
import com.talkie.client.domain.tracking.TrackingService.configureTracking
import com.talkie.client.views.TypedFindView
import com.talkie.client.views.common.views.TypedFindLayout
import net.danlew.android.joda.JodaTimeAndroid


trait Controller extends TypedFindView with TypedFindLayout {

  protected val context: Context
  protected val serviceInterpreter: ServiceInterpreter

  protected def loginButtonOpt: Option[LoginButton] = None
}

trait ControllerImpl extends Controller { self: Activity =>

  private val logger = context.loggerFor(this)

  (for {
    _ <- configureLogin(loginButtonOpt).generalize
    _ <- configureTracking
    _ <- configureNavigation
  } yield {
    JodaTimeAndroid.init(context.androidContext)
    logger debug s"Controller ${self} initialized"
  }).foldMap(serviceInterpreter)
}
