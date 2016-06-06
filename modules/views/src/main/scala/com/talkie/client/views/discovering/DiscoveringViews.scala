package com.talkie.client.views.discovering

import android.support.design.widget.{ FloatingActionButton, NavigationView }
import android.support.v4.widget.DrawerLayout
import android.support.v7.widget.Toolbar
import com.talkie.client.common.components.Activity
import com.talkie.client.views.{ TR, TypedFindView }

private[discovering] trait DiscoveringViews {

  def layout: DrawerLayout
  def floatingActionButton: FloatingActionButton
  def navigationView: NavigationView
  def toolbar: Toolbar
}

private[discovering] final class DiscoveringViewsImpl(activity: Activity) extends DiscoveringViews with TypedFindView {

  override protected def findViewById(id: Int) = activity.findViewById(id)
  override lazy val layout = findView(TR.drawer_layout)
  override lazy val floatingActionButton = findView(TR.fab)
  override lazy val navigationView = findView(TR.nav_view)
  override lazy val toolbar = findView(TR.toolbar)
}
