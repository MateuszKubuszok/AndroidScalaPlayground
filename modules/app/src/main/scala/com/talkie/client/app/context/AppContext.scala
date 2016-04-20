package com.talkie.client.app.context

import com.talkie.client.core.context.Context
import com.talkie.client.core.logging.LoggerImpl

trait AppContext { self: Context =>
}

object AppContextImpl {

  private val logger = new LoggerImpl(this.getClass.getSimpleName)

  logger trace "Global AppContext created"
}

trait AppContextImpl extends AppContext { self: Context =>
}
