package com.talkie.client.app.activities.discovering

import android.support.v7.app.AppCompatActivity
import com.talkie.client.app.activities.common.BaseActivity
import com.talkie.client.app.activities.common.scaloid.support.design.widget.SOnNavigationItemSelectedListener

class DiscoveringActivity
  extends AppCompatActivity
  with BaseActivity
  with SOnNavigationItemSelectedListener
  with DiscoveringViewsImpl
  with DiscoveringController
