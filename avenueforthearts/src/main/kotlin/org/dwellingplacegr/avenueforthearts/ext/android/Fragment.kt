@file:Suppress("NOTHING_TO_INLINE")

package org.dwellingplacegr.avenueforthearts.ext.android

import android.support.v4.app.FragmentTransaction

fun FragmentTransaction.fadeInOut(): FragmentTransaction
  = this.setCustomAnimations(android.R.anim.fade_out, android.R.anim.fade_in)
