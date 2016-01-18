package com.talkie.client.views.settings

import android.content.res.Configuration
import android.os.Bundle
import android.preference.PreferenceActivity
import android.support.annotation.{ LayoutRes, Nullable }
import android.support.v7.app.{ ActionBar, AppCompatDelegate }
import android.support.v7.widget.Toolbar
import android.view.{ MenuInflater, View, ViewGroup }

abstract class AppCompatPreferenceActivity extends PreferenceActivity {

  private lazy val mDelegate = AppCompatDelegate.create(this, null)

  override protected def onCreate(savedInstanceState: Bundle) {
    getDelegate.installViewFactory()
    getDelegate.onCreate(savedInstanceState)
    super.onCreate(savedInstanceState)
  }

  override protected def onPostCreate(savedInstanceState: Bundle) {
    super.onPostCreate(savedInstanceState)
    getDelegate.onPostCreate(savedInstanceState)
  }

  def getSupportActionBar: ActionBar =
    getDelegate.getSupportActionBar

  def setSupportActionBar(@Nullable toolbar: Toolbar) =
    getDelegate.setSupportActionBar(toolbar)

  override def getMenuInflater: MenuInflater =
    getDelegate.getMenuInflater

  override def setContentView(@LayoutRes layoutResID: Int) =
    getDelegate.setContentView(layoutResID)

  override def setContentView(view: View) =
    getDelegate.setContentView(view)

  override def setContentView(view: View, params: ViewGroup.LayoutParams) =
    getDelegate.setContentView(view, params)

  override def addContentView(view: View, params: ViewGroup.LayoutParams) =
    getDelegate.addContentView(view, params)

  protected override def onPostResume() {
    super.onPostResume()
    getDelegate.onPostResume()
  }

  protected override def onTitleChanged(title: CharSequence, color: Int) {
    super.onTitleChanged(title, color)
    getDelegate.setTitle(title)
  }

  override def onConfigurationChanged(newConfig: Configuration) {
    super.onConfigurationChanged(newConfig)
    getDelegate.onConfigurationChanged(newConfig)
  }

  protected override def onStop() {
    super.onStop()
    getDelegate.onStop()
  }

  protected override def onDestroy {
    super.onDestroy()
    getDelegate.onDestroy()
  }

  override def invalidateOptionsMenu = getDelegate.invalidateOptionsMenu()

  private def getDelegate = mDelegate
}
