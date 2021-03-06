package com.talkie.client.views.discovering

import android.support.design.widget.Snackbar
import android.support.v4.view.GravityCompat
import android.support.v7.app.{ ActionBarDrawerToggle, AppCompatActivity }
import android.view.{ Menu, MenuItem, View }
import com.talkie.client.common.components.{ Activity, OnNavigationItemSelectedListener }
import com.talkie.client.common.context.Context
import com.talkie.client.views.R
import com.talkie.client.views.common.Listeners
import com.talkie.client.views.discovering.DrawerOptions.DrawerOption
import com.talkie.client.views.discovering.MenuOptions.MenuOption

trait DiscoveringViewsActions {

  def initializeLayout(): Unit

  def initializeMenu(menu: Menu): Unit

  def closeDrawerIfOpened(): Unit

  def itemToDrawerOption(item: MenuItem): DrawerOption

  def itemToMenuOption(item: MenuItem): MenuOption
}

final class DiscoveringViewsActionsImpl(
    implicit
    context:  Context,
    activity: AppCompatActivity with Activity,
    views:    DiscoveringViews,
    listener: OnNavigationItemSelectedListener
) extends DiscoveringViewsActions {

  private val logger = context.loggerFor(this)

  def initializeLayout(): Unit = {
    activity.setContentView(R.layout.discovering_activity)

    activity.setSupportActionBar(views.toolbar)

    views.floatingActionButton.setOnClickListener(new Listeners.OnClickListener {
      override def onClick(view: View) {
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
          .setAction("Action", null)
          .show()
      }
    })

    val toggle = new ActionBarDrawerToggle(
      activity,
      views.drawer,
      views.toolbar,
      R.string.navigation_drawer_open,
      R.string.navigation_drawer_close
    )
    views.drawer.setDrawerListener(toggle)
    toggle.syncState()

    views.navigationView.setNavigationItemSelectedListener(listener)
  }

  def initializeMenu(menu: Menu): Unit = {
    logger error "initialize fucking menu"
    activity.getMenuInflater.inflate(R.menu.main, menu)
  }

  def closeDrawerIfOpened(): Unit = {
    if (views.layout.isDrawerOpen(GravityCompat.START)) {
      views.layout.closeDrawer(GravityCompat.START)
    }
  }

  def itemToDrawerOption(item: MenuItem): DrawerOption = DrawerOptions.apply(item.getItemId)

  def itemToMenuOption(item: MenuItem): MenuOption = MenuOptions.apply(item.getItemId)
}
