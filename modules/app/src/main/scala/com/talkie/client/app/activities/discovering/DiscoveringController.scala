package com.talkie.client.app.activities.discovering

import android.support.design.widget.Snackbar
import android.support.v4.view.GravityCompat
import android.support.v7.app.{ ActionBarDrawerToggle, AppCompatActivity }
import android.view.View
import com.talkie.client.app.activities.common.Controller
import com.talkie.client.app.navigation.NavigationService.moveToSettings
import com.talkie.client.core.components.{ OnNavigationItemSelectedListener, Activity }
import com.talkie.client.core.facebook.FacebookService.logOutFromFacebook
import com.talkie.client.core.services.ServiceInterpreter._
import com.talkie.client.views.R
import com.talkie.client.views.common.Listeners
import com.talkie.client.views.discovering.DiscoveringViews

trait DiscoveringController extends Controller {
  self: AppCompatActivity with Activity with OnNavigationItemSelectedListener with DiscoveringViews =>

  private implicit val c = context

  onCreate {
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
      layout,
      toolbar,
      R.string.navigation_drawer_open,
      R.string.navigation_drawer_close
    )
    layout.setDrawerListener(toggle)
    toggle.syncState()

    navigationView.setNavigationItemSelectedListener(this)
  }

  onBackPressed {
    if (layout.isDrawerOpen(GravityCompat.START)) {
      layout.closeDrawer(GravityCompat.START)
    }
  }

  onCreateOptionsMenu { menu =>
    getMenuInflater.inflate(R.menu.main, menu)
    true
  }

  onOptionsItemSelected { item =>
    item.getItemId match {
      case R.id.action_settings =>
        moveToSettings.fireAndForget()
        true
      case R.id.action_logout =>
        logOutFromFacebook.fireAndForget()
        true
      case _ => false
    }
  }

  onNavigationItemSelected { item =>
    item.getItemId match {
      case R.id.nav_camera    =>
      case R.id.nav_gallery   =>
      case R.id.nav_slideshow =>
      case R.id.nav_manage    =>
      case R.id.nav_share     =>
      case R.id.nav_send      =>
    }

    layout.closeDrawer(GravityCompat.START)

    true
  }
}
