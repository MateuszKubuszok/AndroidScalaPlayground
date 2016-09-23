package com.talkie.client.views.discovering

import android.support.design.widget.{ FloatingActionButton, NavigationView }
import android.support.v4.widget.DrawerLayout
import android.support.v7.widget.Toolbar
import com.talkie.client.common.components.Activity
import com.talkie.client.views.common.views.TypedFindLayout
import com.talkie.client.views.{ TR, TypedFindView }

trait DiscoveringViews {

  def layout: DrawerLayout
  def floatingActionButton: FloatingActionButton
  def navigationView: NavigationView
  def toolbar: Toolbar
}

final class DiscoveringViewsImpl(
    implicit
    activity: Activity
) extends DiscoveringViews with TypedFindView with TypedFindLayout {

  override protected def findViewById(id: Int) = activity.findViewById(id)
  override lazy val layout = findLayout(TR.layout.discovering_activity)
  override lazy val floatingActionButton = findView(TR.discovering_fab)
  override lazy val navigationView = findView(TR.discovering_nav_view)
  override lazy val toolbar = findView(TR.discovering_toolbar)
}
