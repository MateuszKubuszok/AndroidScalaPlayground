package com.talkie.client.views

import android.app.Activity
import android.support.design.widget.{FloatingActionButton, NavigationView}
import android.support.v4.widget.DrawerLayout
import android.support.v7.widget.Toolbar
import android.view.View
import com.facebook.login.widget.LoginButton
import com.talkie.client.R

private[views] sealed trait Views {

  protected def findView[T](id: Int): Option[T]

  protected def drawerOpt = findView[DrawerLayout](R.id.drawer_layout)
  protected def loginButtonOpt = findView[LoginButton](R.id.login_button)
  protected def fabOpt = findView[FloatingActionButton](R.id.fab)
  protected def navigationOpt = findView[NavigationView](R.id.nav_view)
  protected def toolbarOpt = findView[Toolbar](R.id.toolbar)
}

trait ActivityViews extends Views {
    self: Activity =>

  override def findView[T](id: Int) = Option { self.findViewById(id).asInstanceOf[T] }
}

trait ViewViews extends Views {
    self: View =>

  override def findView[T](id: Int) = Option { self.findViewById(id).asInstanceOf[T] }
}

object Views {

  implicit class ActivityWrapper(activity: Activity) extends Views {

    override def findView[T](id: Int) = Option { activity.findViewById(id).asInstanceOf[T] }
  }

  implicit class ViewWrapper(view: View) extends Views {

    override def findView[T](id: Int) = Option { view.findViewById(id).asInstanceOf[T] }
  }
}
