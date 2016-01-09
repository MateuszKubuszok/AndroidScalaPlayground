package com.talkie.client.app.activities.common.views

import android.util.TypedValue
import android.view.{ View, ViewGroup }
import com.talkie.client.app.activities.common.RichActivity
import org.scaloid.common.TraitViewGroup

trait CommonViews {
  self: RichActivity =>

  private def findColor(id: Int) = getResources.getColor(id, null)
  private def findDimension(id: Int) = getResources.getDimensionPixelSize(id)
  private def findDrawable(id: Int) = getResources.getDrawable(id, null)
  private def findStyle(id: Int)(viewGroup: TraitViewGroup[_ <: ViewGroup]) = { view: View =>
    viewGroup.applyStyle(findViewById(id))
  }
  private def resolve(id: Int) = {
    val typedValue = new TypedValue
    getTheme.resolveAttribute(id, typedValue, true)
    typedValue.resourceId
  }

  object attribute {
    import com.talkie.client.R.{ attr => a }

    lazy val actionBarSize = findDimension(resolve(a.actionBarSize))
  }

  object color {
    import com.talkie.client.R.color._

    lazy val accent = findColor(colorAccent)

    lazy val primary = findColor(colorPrimary)
    lazy val primaryDark = findColor(colorPrimaryDark)
  }

  object dimension {
    import com.talkie.client.R.dimen._

    lazy val activityHorizontalMargin = findDimension(activity_horizontal_margin)
    lazy val activityVerticalMargin = findDimension(activity_vertical_margin)

    lazy val floatingActionButtonMargin = findDimension(fab_margin)

    lazy val loginButtonVerticalMargin = findDimension(login_button_vertical_margin)

    lazy val navigationHeaderHeight = findDimension(nav_header_height)
    lazy val navigationHeaderVerticalSpacing = findDimension(nav_header_vertical_spacing)
  }

  object drawables {
    import android.R.drawable._
    import com.talkie.client.R.drawable._

    lazy val dialogEmail = findDrawable(ic_dialog_email)
    lazy val sideNavBar = findDrawable(side_nav_bar)
    lazy val symDefAppIcon = findDrawable(sym_def_app_icon)
  }

  object themes {
    import com.talkie.client.R.style._

    object AppTheme {

      lazy val AppBarOverlay = findStyle(AppTheme_AppBarOverlay) _
      lazy val PopupOverlay = findStyle(AppTheme_PopupOverlay) _
    }

    object TextAppearance {

      object AppCompat {

        lazy val Body1 = TextAppearance_AppCompat_Body1
      }
    }

    object ThemeOverlay {

      object AppCompat {

        lazy val Dark = findStyle(ThemeOverlay_AppCompat_Dark) _
      }
    }
  }

  implicit class AppendView[T <: View](view: T) {

    def appendTo[S <: ViewGroup](viewGroup: S) = {
      viewGroup.addView(view)
      view
    }
  }
}
