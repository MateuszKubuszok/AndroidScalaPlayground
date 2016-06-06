package com.talkie.client.views.discovering

import com.talkie.client.views.R

object DrawerOptions extends Enumeration {
  type DrawerOption = Value
  val Camera = Value(R.id.nav_camera)
  val Gallery = Value(R.id.nav_gallery)
  val SlideShow = Value(R.id.nav_slideshow)
  val Manage = Value(R.id.nav_manage)
  val Share = Value(R.id.nav_share)
  val Send = Value(R.id.nav_send)
}
