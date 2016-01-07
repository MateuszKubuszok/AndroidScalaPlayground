package com.talkie.client.app.activities.common

trait CommonViews {
  self: RichActivity =>

  private def getDimension(id: Int) = getResources.getDimensionPixelSize(id)

  object dimens {
    import com.talkie.client.R.dimen._
    val activityHorizontalMargin = getDimension(activity_horizontal_margin)
    val activityVerticalMargin = getDimension(activity_vertical_margin)

    val loginButtonVerticalMargin = getDimension(login_button_vertical_margin)
  }
}
