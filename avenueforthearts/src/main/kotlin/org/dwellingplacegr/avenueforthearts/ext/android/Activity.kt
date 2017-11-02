@file:Suppress("NOTHING_TO_INLINE")

package org.dwellingplacegr.avenueforthearts.ext.android

import android.app.Activity
import android.content.Intent
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat
import org.jetbrains.anko.inputMethodManager

inline val Activity.fingerprintManager: FingerprintManagerCompat
  get() = FingerprintManagerCompat.from(this)

inline fun Activity.finish(code: Int, result: Intent = intentWith()) {
  setResult(code, result)
  finish()
}

fun Activity.restartApplication() {
  val intent = Intent()
  intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
  intent.action = Intent.ACTION_MAIN
  intent.`package` = packageName
  startActivity(intent)
}

fun Activity.hideSoftKeyboard() {
  currentFocus?.windowToken?.let {
    inputMethodManager.hideSoftInputFromWindow(it, 0)
  }
}
