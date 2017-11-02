package org.dwellingplacegr.avenueforthearts.ext.android

import android.content.SharedPreferences
import org.joda.time.DateTime

private val EPOCH = DateTime(0)

fun SharedPreferences.getDateTime(key: String, default: DateTime = EPOCH): DateTime {
  val millis = this.getLong(key, default.millis)
  return DateTime(millis)
}
