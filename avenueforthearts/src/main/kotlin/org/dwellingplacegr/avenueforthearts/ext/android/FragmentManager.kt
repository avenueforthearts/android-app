package org.dwellingplacegr.avenueforthearts.ext.android

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager

inline fun <reified T: Fragment> FragmentManager.findFragment(tag: String): T?
  = findFragmentByTag(tag) as? T
