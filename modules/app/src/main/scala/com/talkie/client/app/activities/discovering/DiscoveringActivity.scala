package com.talkie.client.app.activities.discovering

import android.support.v7.app.AppCompatActivity
import com.talkie.client.app.activities.common.BaseActivity
import com.talkie.client.core.components.OnNavigationItemSelectedListener
import com.talkie.client.views.discovering.DiscoveringViewsImpl

class DiscoveringActivity
  extends AppCompatActivity
  with BaseActivity
  with OnNavigationItemSelectedListener
  with DiscoveringViewsImpl
  with DiscoveringController
