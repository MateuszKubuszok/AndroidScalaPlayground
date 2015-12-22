package com.talkie.client

import android.os.Bundle
import android.support.design.widget.{NavigationView, Snackbar}
import android.support.v4.view.GravityCompat
import android.support.v7.app.{ActionBarDrawerToggle, AppCompatActivity}
import android.view.{Menu, MenuItem}
import com.talkie.client.common.{ActivityViews, ListenerUtils}

class MainActivity
    extends AppCompatActivity
    with NavigationView.OnNavigationItemSelectedListener
    with ActivityViews
    with ListenerUtils {

  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
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

  override def onBackPressed() = drawerOpt foreach { drawer =>
    if (drawer.isDrawerOpen(GravityCompat.START)) drawer.closeDrawer(GravityCompat.START)
    else super.onBackPressed()
  }

  override def onCreateOptionsMenu(menu: Menu): Boolean = {
    getMenuInflater.inflate(R.menu.main, menu)
    true
  }

  override def onOptionsItemSelected(item: MenuItem): Boolean =
    (item.getItemId == R.id.action_settings) || super.onOptionsItemSelected(item)

  def onNavigationItemSelected(item: MenuItem): Boolean = {
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
