package com.talkie.client.app.activities.discovering

import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener
import android.support.v4.view.GravityCompat
import android.support.v7.app.{ ActionBarDrawerToggle, AppCompatActivity }
import android.view.{ View, MenuItem, Menu }
import com.talkie.client.R
import com.talkie.client.TR
import com.talkie.client.app.activities.common.{ RichActivity, Listeners, Controller }
import com.talkie.client.domain.services.facebook.FacebookMessages.LogoutRequest
import com.talkie.client.domain.services.facebook.FacebookServicesComponent

trait DiscoveringController extends Controller {
  self: AppCompatActivity with RichActivity with FacebookServicesComponent with OnNavigationItemSelectedListener =>

  private implicit val c = context

  protected lazy val drawerLayout = findView(TR.drawer_layout)
  protected lazy val floatingActionButton = findView(TR.fab)
  protected lazy val navigationView = findView(TR.nav_view)
  protected lazy val toolbar = findView(TR.toolbar)

  final protected def onCreateEvent() = {
    setContentView(R.layout.activity_discovering)

    setSupportActionBar(toolbar)

    floatingActionButton.setOnClickListener(new Listeners.OnClickListener {
      override def onClick(view: View) {
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
          .setAction("Action", null)
          .show()
      }
    })

    val toggle = new ActionBarDrawerToggle(
      this,
      drawerLayout,
      toolbar,
      R.string.navigation_drawer_open,
      R.string.navigation_drawer_close
    )
    drawerLayout.setDrawerListener(toggle)
    toggle.syncState()

    navigationView.setNavigationItemSelectedListener(this)
  }

  final protected def onBackPressedEvent() =
    if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
      drawerLayout.closeDrawer(GravityCompat.START)
      true
    } else {
      false
    }

  final protected def onCreateOptionsMenuEvent(menu: Menu) = {
    getMenuInflater.inflate(R.menu.main, menu)
    true
  }

  final protected def onOptionsItemSelectedEvent(item: MenuItem) = item.getItemId match {
    case R.id.action_settings =>
      startSettingsActivity()
      true
    case R.id.action_logout =>
      facebookServices.logout(LogoutRequest())
      true
    case _ => false
  }

  final protected def onNavigationItemSelectedEvent(item: MenuItem) = {
    item.getItemId match {
      case R.id.nav_camera    =>
      case R.id.nav_gallery   =>
      case R.id.nav_slideshow =>
      case R.id.nav_manage    =>
      case R.id.nav_share     =>
      case R.id.nav_send      =>
    }

    drawerLayout.closeDrawer(GravityCompat.START)

    true
  }
}
