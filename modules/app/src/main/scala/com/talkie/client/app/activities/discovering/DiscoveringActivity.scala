package com.talkie.client.app.activities.discovering

import com.talkie.client.app.activities.common.AppActivity
import com.talkie.client.app.navigation.NavigationServiceTaskInterpreter
import com.talkie.client.common.components.OnNavigationItemSelectedListener
import com.talkie.client.common.services.EnrichNTOps._
import com.talkie.client.core.facebook.FacebookServiceTaskInterpreter
import com.talkie.client.views.discovering._

import scalaz._
import scalaz.concurrent.Task

final class DiscoveringActivity extends AppActivity with OnNavigationItemSelectedListener with DiscoveringController {

  implicit val listener = this
  implicit val loginViews: DiscoveringViews = new DiscoveringViewsImpl
  implicit val loginViewsActions: DiscoveringViewsActions = new DiscoveringViewsActionsImpl

  val i0: S0 ~> Task = new FacebookServiceTaskInterpreter
  val i1: S1 ~> Task = new NavigationServiceTaskInterpreter :+: i0
  val i2: S2 ~> Task = new DiscoveringViewsServiceTaskInterpreter :+: i1

  val interpreter: Eff ~> Task = i2

  onCreate { _ =>
    initializeLayout.foldMap(interpreter).unsafePerformSync
  }

  onCreateOptionsMenu { menu =>
    initializeMenu(menu).foldMap(interpreter).unsafePerformSyncAttempt.isRight
  }

  onBackPressed {
    closeDrawerIfOpened.foldMap(interpreter).unsafePerformSyncAttempt.isRight
  }

  onOptionsItemSelected { menuItem =>
    handleMenuOptions(menuItem).foldMap(interpreter).unsafePerformSyncAttempt.isRight
  }

  onNavigationItemSelected { item =>
    handleDrawerOptions(item).foldMap(interpreter).unsafePerformSyncAttempt.isRight
  }
}
