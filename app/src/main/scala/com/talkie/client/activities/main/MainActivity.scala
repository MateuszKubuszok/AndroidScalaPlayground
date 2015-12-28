package com.talkie.client.activities.main

import android.os.Bundle
import android.support.design.widget.{NavigationView, Snackbar}
import android.support.v4.view.GravityCompat
import android.support.v7.app.{ActionBarDrawerToggle, AppCompatActivity}
import android.view.{Menu, MenuItem}
import com.talkie.client.R
import com.talkie.client.activities.common.BaseActivity
import com.talkie.client.domain.services.facebook.FacebookMessages.LogoutRequest
import com.talkie.client.views.{ActivityViews, Listeners}

class MainActivity
    extends AppCompatActivity
    with NavigationView.OnNavigationItemSelectedListener
    with BaseActivity
    with MainController {

  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    onCreateEvent()
  }

  override def onBackPressed() = onBackPressedEvent() || { super.onBackPressed(); false }

  override def onCreateOptionsMenu(menu: Menu) =
    onCreateOptionsMenuEvent(menu) || super.onCreateOptionsMenu(menu)

  override def onOptionsItemSelected(item: MenuItem) =
    onOptionsItemSelectedEvent(item) || super.onOptionsItemSelected(item)

  override def onNavigationItemSelected(item: MenuItem) =
    onNavigationItemSelectedEvent(item)
}
