package com.talkie.client.domain.context

import com.talkie.client.core.context.Context

trait DomainContext { self: Context =>
}

trait DomainContextImpl extends DomainContext { self: Context =>
}
