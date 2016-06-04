package com.talkie.client.app.activities.common

import android.content.Context
import com.facebook.login.widget.LoginButton
import com.talkie.client.app.navigation.Service._
import com.talkie.client.common.logging.Logger
import com.talkie.client.core.facebook.Service._
import com.talkie.client.domain.tracking.Service._
import com.talkie.client.views.TypedFindView
import com.talkie.client.views.common.views.TypedFindLayout
import net.danlew.android.joda.JodaTimeAndroid

trait Controller extends TypedFindView with TypedFindLayout {

  protected val logger: Logger
  protected def loginButtonOpt(): Option[LoginButton] = None

  final protected def initializeController(androidContext: Context) = for {
    _ <- configureTracking
    _ <- configureNavigation
    _ <- configureLogin(loginButtonOpt)
  } yield {
    JodaTimeAndroid.init(androidContext) // TODO: remember what requires this (Slick?) and move it out of here
    logger debug s"Controller initialized"
  }
}
