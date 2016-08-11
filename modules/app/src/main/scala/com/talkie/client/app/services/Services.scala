package com.talkie.client.app.services

import com.talkie.client.app.navigation.NavigationService
import com.talkie.client.core.events.EventService
import com.talkie.client.core.facebook.FacebookService
import com.talkie.client.core.location.LocationService
import com.talkie.client.core.permissions.PermissionService
import com.talkie.client.core.scheduler.SchedulerService
import com.talkie.client.domain.tracking.TrackingService

import scalaz._

object Services {

  type C0[A] = EventService[A]
  type C1[A] = Coproduct[FacebookService, C0, A]
  type C2[A] = Coproduct[LocationService, C1, A]
  type C3[A] = Coproduct[PermissionService, C2, A]
  type C4[A] = Coproduct[SchedulerService, C3, A]

  type D0[A] = Coproduct[TrackingService, C4, A]

  type A0[A] = Coproduct[NavigationService, D0, A]

  type Eff[A] = A0[A]
  type ViewEff[ViewService[_], A] = Coproduct[ViewService, Eff, A]

  class EffServices[S[_]](
      implicit
      c0: EventService :<: S,
      c1: FacebookService :<: S,
      c2: LocationService :<: S,
      c3: PermissionService :<: S,
      c4: SchedulerService :<: S,
      d0: TrackingService :<: S,
      a0: NavigationService :<: S
  ) {

    val eventService = new EventService.Ops[S]
    val facebookService = new FacebookService.Ops[S]
    val locationService = new LocationService.Ops[S]
    val permissionService = new PermissionService.Ops[S]
    val schedulerService = new SchedulerService.Ops[S]

    val trackingService = new TrackingService.Ops[S]

    val navigationService = new NavigationService.Ops[S]
  }
  object EffServices extends EffServices[Eff]
}
