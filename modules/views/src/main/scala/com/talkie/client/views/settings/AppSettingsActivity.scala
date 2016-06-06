package com.talkie.client.views.settings

import android.content.res.Configuration
import android.preference.PreferenceActivity
import android.support.annotation.{ LayoutRes, Nullable }
import android.support.v7.app.{ ActionBar, AppCompatDelegate }
import android.support.v7.widget.Toolbar
import android.view.{ MenuInflater, View, ViewGroup }
import com.talkie.client.common.components.Activity

trait AppSettingsActivity extends PreferenceActivity with Activity {

  private lazy val delegate = AppCompatDelegate.create(this, null)

  onCreate { savedInstanceState =>
    delegate.installViewFactory()
    delegate.onCreate(savedInstanceState.orNull)
  }

  onPostCreate { savedInstanceState =>
    delegate.onPostCreate(savedInstanceState.orNull)
  }

  onPostResume {
    delegate.onPostResume()
  }

  onStop {
    delegate.onStop()
  }

  onDestroy {
    delegate.onDestroy()
  }

  def getSupportActionBar: ActionBar = delegate.getSupportActionBar

  def setSupportActionBar(@Nullable toolbar: Toolbar) = delegate.setSupportActionBar(toolbar)

  override def getMenuInflater: MenuInflater = delegate.getMenuInflater

  override def setContentView(@LayoutRes layoutResID: Int) = delegate.setContentView(layoutResID)

  override def setContentView(view: View) = delegate.setContentView(view)

  override def setContentView(view: View, params: ViewGroup.LayoutParams) = delegate.setContentView(view, params)

  override def addContentView(view: View, params: ViewGroup.LayoutParams) = delegate.addContentView(view, params)

  protected override def onTitleChanged(title: CharSequence, color: Int) {
    super.onTitleChanged(title, color)
    delegate.setTitle(title)
  }

  override def onConfigurationChanged(newConfig: Configuration) {
    super.onConfigurationChanged(newConfig)
    delegate.onConfigurationChanged(newConfig)
  }

  override def invalidateOptionsMenu = delegate.invalidateOptionsMenu()
}
