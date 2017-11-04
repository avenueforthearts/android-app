package org.dwellingplacegr.avenueforthearts.ui

import android.content.Context
import android.view.MotionEvent
import android.support.v4.view.ViewPager
import android.util.AttributeSet


class LockableViewPager: ViewPager {

  var swipeLocked: Boolean = true

  constructor(context: Context) : super(context)

  constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

  override fun onTouchEvent(event: MotionEvent): Boolean {
    return !swipeLocked && super.onTouchEvent(event)
  }

  override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
    return !swipeLocked && super.onInterceptTouchEvent(event)
  }

  override fun canScrollHorizontally(direction: Int): Boolean {
    return !swipeLocked && super.canScrollHorizontally(direction)
  }
}
