package com.talkie.client.app.activities.common

import com.talkie.client.app.services.ServiceInterpreterImpl
import com.talkie.client.common.components.ActivityImpl
import com.talkie.client.common.services.ServiceInterpreter._

trait BaseActivity extends ActivityImpl { self: Controller =>

  override protected implicit val serviceInterpreter = new ServiceInterpreterImpl(context, this)

  onCreate { _ =>
    initializeController(context.androidContext).fireAndWait()
  }
}
