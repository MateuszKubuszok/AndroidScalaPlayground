package com.talkie.client.app.initialization

import com.talkie.client.app.activities.common.Controller
import com.talkie.client.views.common.RichActivity

trait AppInitialization {
  self: RichActivity with Controller =>

  onCreate {
    Seq(
      new JodaTimeInitializer(self, context),
      new FacebookInitializer(self, context),
      //      new LocationInitializer(context, scheduler, locationJobs),
      new AuthNavigationInitializer(context, eventBus, authNavigation)
    ) foreach (_.initialize())
  }
}
