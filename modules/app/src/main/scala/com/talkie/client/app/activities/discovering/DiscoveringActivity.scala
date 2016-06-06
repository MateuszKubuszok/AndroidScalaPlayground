package com.talkie.client.app.activities.discovering

import com.talkie.client.app.activities.common.AppActivity
import com.talkie.client.common.components.OnNavigationItemSelectedListener
import com.talkie.client.common.services.ServiceInterpreter._
import com.talkie.client.views.discovering.DiscoveringViewsServiceInterpreterImpl

final class DiscoveringActivity extends AppActivity with OnNavigationItemSelectedListener with DiscoveringController {

  override protected val viewInterpreter = new DiscoveringViewsServiceInterpreterImpl(context, this, this).forService

  onCreate { _ =>
    initializeLayout.fireAndWait()
  }

  onCreateOptionsMenu { menu =>
    initializeMenu(menu).fireAndWait().isRight
  }

  onBackPressed {
    closeDrawerIfOpened.fireAndWait().isRight
  }

  onOptionsItemSelected { menuItem =>
    handleMenuOptions(menuItem).fireAndWait().isRight
  }

  onNavigationItemSelected { item =>
    handleDrawerOptions(item).fireAndWait().isRight
  }
}
