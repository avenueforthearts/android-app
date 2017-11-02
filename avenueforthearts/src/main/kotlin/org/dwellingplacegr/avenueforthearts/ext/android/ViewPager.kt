package org.dwellingplacegr.avenueforthearts.ext.android

import android.support.v4.view.ViewPager

inline fun ViewPager.onPageChanged(crossinline listener: (position: Int) -> Unit) {
  addOnPageChangeListener(object: ViewPager.SimpleOnPageChangeListener() {
    override fun onPageSelected(position: Int) {
      listener(position)
    }
  })
}
