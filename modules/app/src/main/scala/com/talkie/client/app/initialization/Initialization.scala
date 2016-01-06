package com.talkie.client.app.initialization

import scala.collection.mutable.ArrayBuffer

private[initialization] trait Initialization {

  protected def initializeApp() = appInitializationBodies.foreach(_())

  protected val appInitializationBodies = new ArrayBuffer[() => Any]

  protected def onInitialization(body: => Any) = {
    val el = body _
    appInitializationBodies += el
    el
  }
}
