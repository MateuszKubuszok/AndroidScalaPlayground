package com.talkie.client.app.context

import com.talkie.client.core.context.Context

trait AppContext { self: Context =>
}

trait AppContextImpl extends AppContext { self: Context =>
}
