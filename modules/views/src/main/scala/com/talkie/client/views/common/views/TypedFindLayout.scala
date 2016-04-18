package com.talkie.client.views.common.views

import android.view.View
import com.talkie.client.views.TypedLayout

trait TypedFindLayout {

  protected def findViewById(id: Int): View
  final def findLayout[A](tr: TypedLayout[A]): A = findViewById(tr.id).asInstanceOf[A]
}
