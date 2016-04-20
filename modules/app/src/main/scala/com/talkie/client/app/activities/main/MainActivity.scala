package com.talkie.client.app.activities.main

import android.support.v7.app.AppCompatActivity
import com.talkie.client.app.activities.common.BaseActivity
import com.talkie.client.views.main.MainViewsImpl

class MainActivity
  extends AppCompatActivity
  with BaseActivity
  with MainController
  with MainViewsImpl
