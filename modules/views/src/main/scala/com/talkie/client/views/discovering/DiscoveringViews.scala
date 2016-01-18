package com.talkie.client.views.discovering

import android.support.design.widget.{ FloatingActionButton, NavigationView }
import android.support.v4.widget.DrawerLayout
import android.support.v7.widget.Toolbar
import com.talkie.client.views.common.RichActivity
import com.talkie.client.views.{ TR, TypedFindView }

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

class DiscoveringViewsMock extends RichActivity with TypedFindView with DiscoveringViewsImpl
