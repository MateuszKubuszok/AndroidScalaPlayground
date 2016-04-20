package com.talkie.client.domain.context

import com.talkie.client.core.context.Context
import com.talkie.client.core.logging.LoggerImpl

trait DomainContext { self: Context =>
}

object DomainContextImpl {

  private val logger = new LoggerImpl(this.getClass.getSimpleName)

  logger trace "Global DomainContext created"
}

trait DomainContextImpl extends DomainContext { self: Context =>
}
