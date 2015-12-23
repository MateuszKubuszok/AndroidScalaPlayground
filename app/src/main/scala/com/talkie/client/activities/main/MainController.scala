package com.talkie.client.activities.main

import android.support.design.widget.{NavigationView, Snackbar}
import android.support.v4.view.GravityCompat
import android.support.v7.app.{ActionBarDrawerToggle, AppCompatActivity}
import android.view.{MenuItem, Menu}
import com.talkie.client.R
import com.talkie.client.activities.common.Controller
import com.talkie.client.services.facebook.FacebookMessages.LogoutRequest
import com.talkie.client.services.facebook.FacebookServicesComponent
import com.talkie.client.views.Listeners

trait MainController extends Controller {
  self: AppCompatActivity
    with FacebookServicesComponent
    with Listeners
    with NavigationView.OnNavigationItemSelectedListener =>

  private implicit val c = context

  protected def onCreateEvent() {
    setContentView(R.layout.activity_main)

    toolbarOpt foreach setSupportActionBar

    fabOpt foreach setOnClickListener {
      Snackbar.make(_, "Replace with your own action", Snackbar.LENGTH_LONG)
        .setAction("Action", null)
        .show()
    }

    for {
      toolbar <- toolbarOpt
      drawer <- drawerOpt
      toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
    } {
      drawer.setDrawerListener(toggle)
      toggle.syncState()
    }

    navigationOpt foreach { _.setNavigationItemSelectedListener(this) }
  }

  protected def onBackPressedEvent() = drawerOpt exists { drawer =>
    if (drawer.isDrawerOpen(GravityCompat.START)) {
      drawer.closeDrawer(GravityCompat.START)
      true
    } else false
  }

  protected def onCreateOptionsMenuEvent(menu: Menu): Boolean = {
    getMenuInflater.inflate(R.menu.main, menu)
    true
  }

  protected def onOptionsItemSelectedEvent(item: MenuItem) = item.getItemId match {
    case R.id.action_settings => true
    case R.id.action_logout =>
      facebookServices.logout(LogoutRequest())
      true
    case _ => false
  }

  protected def onNavigationItemSelectedEvent(item: MenuItem) = {
    item.getItemId match {
      case R.id.nav_camera =>
      case R.id.nav_gallery =>
      case R.id.nav_slideshow =>
      case R.id.nav_manage =>
      case R.id.nav_share =>
      case R.id.nav_send =>
    }

    drawerOpt foreach { _.closeDrawer(GravityCompat.START) }

    true
  }
}
