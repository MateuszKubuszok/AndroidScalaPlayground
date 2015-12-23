package com.talkie.client.activities.common

import android.app.Activity
import com.talkie.client.navigation.NavigateActivities
import com.talkie.client.services.LoggerComponentImpl
import com.talkie.client.views.ActivityViews

trait BaseActivity
    extends Activity
    with ActivityViews
    with NavigateActivities
    with LoggerComponentImpl
