package com.talkie.client.views.common.scaloid.support.design.widget

import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener
import android.view.MenuItem

import scala.collection.mutable.ArrayBuffer

trait SOnNavigationItemSelectedListener extends OnNavigationItemSelectedListener {

  override def onNavigationItemSelected(menuItem: MenuItem): Boolean =
    onNavigationItemSelectedBodies map (body => body(menuItem)) exists identity

  protected val onNavigationItemSelectedBodies = new ArrayBuffer[MenuItem => Boolean]

  protected def onNavigationItemSelected(body: MenuItem => Boolean) = {
    onNavigationItemSelectedBodies += body
    body
  }
}
