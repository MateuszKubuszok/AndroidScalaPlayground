package com.talkie.client.app.activities.main

import android.support.v7.app.AppCompatActivity
import com.talkie.client.app.activities.common.BaseActivity
import com.talkie.client.app.initialization.AppInitialization

class MainActivity
    extends AppCompatActivity
    with BaseActivity
    with AppInitialization
    with MainController
    with MainViewsImpl {

  onCreate {
    setTitle("Main")
  }
}
