package org.dwellingplacegr.avenueforthearts.ui

import android.content.Context
import android.view.MotionEvent
import android.widget.ScrollView


class LockableScrollView(context: Context?) : ScrollView(context) {

  // true if we can scroll the ScrollView
  // false if we cannot scroll
  var isScrollable = true

  override fun onTouchEvent(ev: MotionEvent): Boolean {
    return when (ev.action) {
      MotionEvent.ACTION_DOWN -> {
        if (isScrollable) super.onTouchEvent(ev) else isScrollable
      }
      else -> super.onTouchEvent(ev)
    }
  }

  override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
    return when (!isScrollable) {
      false -> false
      true -> super.onInterceptTouchEvent(ev)
    }
  }

}
