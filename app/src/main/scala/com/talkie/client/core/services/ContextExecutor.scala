package com.talkie.client.core.services

import java.util.concurrent.TimeUnit.SECONDS
import java.util.concurrent.{SynchronousQueue, ThreadPoolExecutor}

object ContextExecutor
    extends ThreadPoolExecutor(8, 16, 30, SECONDS, new SynchronousQueue[Runnable]())
