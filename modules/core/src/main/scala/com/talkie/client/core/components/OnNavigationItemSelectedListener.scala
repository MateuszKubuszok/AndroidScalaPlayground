package com.talkie.client.core.components

import android.view.MenuItem

import scala.collection.mutable.ArrayBuffer

trait OnNavigationItemSelectedListener
    extends android.support.design.widget.NavigationView.OnNavigationItemSelectedListener {

  override def onNavigationItemSelected(menuItem: MenuItem): Boolean =
    onNavigationItemSelectedBodies map (body => body(menuItem)) exists identity

  protected val onNavigationItemSelectedBodies = new ArrayBuffer[MenuItem => Boolean]

  protected def onNavigationItemSelected(body: MenuItem => Boolean) = {
    onNavigationItemSelectedBodies += body
    body
  }
}
