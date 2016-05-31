package com.talkie.client.app.activities.common

import com.facebook.login.widget.LoginButton
import com.talkie.client.app.navigation.NavigationService._
import com.talkie.client.app.services.ServiceInterpreterImpl
import com.talkie.client.core.components.Activity
import com.talkie.client.core.context.{ ContextImpl, Context }
import com.talkie.client.core.facebook.FacebookService._
import com.talkie.client.core.logging.Logger
import com.talkie.client.core.services._
import com.talkie.client.core.services.ServiceInterpreter._
import com.talkie.client.domain.tracking.TrackingService._
import com.talkie.client.views.TypedFindView
import com.talkie.client.views.common.views.TypedFindLayout
import net.danlew.android.joda.JodaTimeAndroid

import scalaz.-\/

trait Controller extends TypedFindView with TypedFindLayout {

  protected val context: Context
  protected implicit val serviceInterpreter: ServiceInterpreter

  protected val logger: Logger

  protected def loginButtonOpt(): Option[LoginButton] = None
}

trait ControllerImpl extends Controller { self: Activity =>

  override protected val context = ContextImpl(this)
  override protected implicit val serviceInterpreter = new ServiceInterpreterImpl(context, this)

  override protected val logger = context.loggerFor(this)

  onCreate {
    val result = (for {
      _ <- configureTracking.generalize
      _ <- configureNavigation.generalize
      _ <- configureLogin(loginButtonOpt).generalize
    } yield {
      JodaTimeAndroid.init(context.androidContext)
      logger debug s"Controller ${self} initialized"
    }).fireAndWait()
  }
}
