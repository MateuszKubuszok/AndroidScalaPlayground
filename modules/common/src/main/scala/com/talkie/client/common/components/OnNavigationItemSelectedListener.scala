package com.talkie.client.common.components

import android.view.MenuItem
import android.support.design.widget.NavigationView.{ OnNavigationItemSelectedListener => Listener }

import scala.collection.mutable.ArrayBuffer

trait OnNavigationItemSelectedListener extends Listener {

  private val onNavigationItemSelectedBodies = new ArrayBuffer[MenuItem => Boolean]
  protected def onNavigationItemSelected(body: MenuItem => Boolean): Unit =
    onNavigationItemSelectedBodies += body
  override def onNavigationItemSelected(menuItem: MenuItem): Boolean =
    onNavigationItemSelectedBodies map (body => body(menuItem)) exists identity
}
