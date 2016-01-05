package com.talkie.client.app.activities.discovering

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v7.app.AppCompatActivity
import android.view.{ Menu, MenuItem }
import com.talkie.client.app.activities.common.BaseActivity

class DiscoveringActivity
    extends AppCompatActivity
    with NavigationView.OnNavigationItemSelectedListener
    with BaseActivity
    with DiscoveringController {

  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    onCreateEvent()
  }

  override def onBackPressed() = onBackPressedEvent() || onBackPressedNotConsumed()

  override def onCreateOptionsMenu(menu: Menu) =
    onCreateOptionsMenuEvent(menu) || super.onCreateOptionsMenu(menu)

  override def onOptionsItemSelected(item: MenuItem) =
    onOptionsItemSelectedEvent(item) || super.onOptionsItemSelected(item)

  override def onNavigationItemSelected(item: MenuItem) =
    onNavigationItemSelectedEvent(item)

  private def onBackPressedNotConsumed() = {
    super.onBackPressed()
    false
  }
}
