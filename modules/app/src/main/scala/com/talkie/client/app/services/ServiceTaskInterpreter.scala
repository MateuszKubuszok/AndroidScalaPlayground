package com.talkie.client.app.services

import com.talkie.client.app.navigation._
import com.talkie.client.common.components.Activity
import com.talkie.client.common.context.Context
import com.talkie.client.common.services.EnrichNTOps._
import com.talkie.client.core.events._
import com.talkie.client.core.facebook._
import com.talkie.client.core.location._
import com.talkie.client.core.permissions._
import com.talkie.client.core.scheduler._
import com.talkie.client.domain.tracking._

import scalaz._
import scalaz.concurrent.Task

trait ServiceTaskInterpreter { self: Activity =>

  protected implicit val context: Context
  protected implicit val activity: Activity = self

  protected implicit val eventActions: EventActions = new EventActionsImpl
  protected implicit val facebookActions: FacebookActions = new FacebookActionsImpl
  protected implicit val permissionActions: PermissionActions = new PermissionActionsImpl
  protected implicit val locationActions: LocationActions = new LocationActionsImpl
  protected implicit val schedulerActions: SchedulerActions = new SchedulerActionsImpl
  protected implicit val trackingActions: TrackingActions = new TrackingActionsImpl
  protected implicit val navigationActions: NavigationActions = new NavigationActionsImpl

  private val c0: Services.C0 ~> Task = new EventServiceTaskInterpreter
  private val c1: Services.C1 ~> Task = new FacebookServiceTaskInterpreter :+: c0
  private val c2: Services.C2 ~> Task = new LocationServiceTaskInterpreter :+: c1
  private val c3: Services.C3 ~> Task = new PermissionServiceTaskInterpreter :+: c2
  private val c4: Services.C4 ~> Task = new SchedulerServiceTaskInterpreter :+: c3

  private val d0: Services.D0 ~> Task = new TrackingServiceTaskInterpreter :+: c4

  private val a0: Services.A0 ~> Task = new NavigationServiceTaskInterpreter :+: d0

  protected val effInterpreter: Services.Eff ~> Task = a0

  implicit class ViewTaskInterpreterBuilder[ViewService[_]](viewServiceTaskInterpreter: ViewService ~> Task) {

    type ViewEff[A] = Services.ViewEff[ViewService, A]

    def intoViewEffTaskInterpreter(): ViewEff ~> Task = viewServiceTaskInterpreter :+: effInterpreter
  }
}
