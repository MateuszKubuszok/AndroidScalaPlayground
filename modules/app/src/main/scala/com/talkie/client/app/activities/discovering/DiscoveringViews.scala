package com.talkie.client.app.activities.discovering

import android.support.design.widget.{ NavigationView, FloatingActionButton }
import android.support.v4.widget.DrawerLayout
import android.support.v7.widget.Toolbar
import com.talkie.client.{ TypedFindView, TR }

trait DiscoveringViews {

  protected def layout: DrawerLayout
  protected def floatingActionButton: FloatingActionButton
  protected def navigationView: NavigationView
  protected def toolbar: Toolbar
}

trait DiscoveringViewsImpl extends DiscoveringViews {
  self: TypedFindView =>

  override protected lazy val layout = findView(TR.drawer_layout)
  override protected lazy val floatingActionButton = findView(TR.fab)
  override protected lazy val navigationView = findView(TR.nav_view)
  override protected lazy val toolbar = findView(TR.toolbar)
}
