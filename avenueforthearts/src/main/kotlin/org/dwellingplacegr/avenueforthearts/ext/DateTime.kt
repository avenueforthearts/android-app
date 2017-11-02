@file:Suppress("HasPlatformType")

package org.dwellingplacegr.avenueforthearts.ext

import org.joda.time.*
import org.joda.time.format.DateTimeFormat
import java.util.*

inline val Int.seconds get() = Seconds.seconds(this)
inline val Int.minutes get() = Minutes.minutes(this)
inline val Int.hours get() = Hours.hours(this)
inline val Int.days get() = Days.days(this)
inline val Int.weeks get() = Weeks.weeks(this)
inline val Int.months get() = Months.months(this)
inline val Int.years get() = Years.years(this)

@Suppress("NOTHING_TO_INLINE")
inline fun now() = DateTime.now()

private val FMT_ISO_8601_uS = DateTimeFormat
  .forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSZ")
  .withLocale(Locale.US)

fun String.asISO8601DateTime() = FMT_ISO_8601_uS.parseDateTime(this)
val ReadableInstant.iso8601 get() = FMT_ISO_8601_uS.print(this)

data class DateTimeRange(val start: DateTime, val end: DateTime) {
  inline val seconds get() = Seconds.secondsBetween(start, end).seconds
  inline val minutes get() = Minutes.minutesBetween(start, end).minutes
  inline val hours get() = Hours.hoursBetween(start, end).hours
  inline val days get() = Days.daysBetween(start, end).days
  inline val weeks get() = Weeks.weeksBetween(start, end).weeks
  inline val months get() = Months.monthsBetween(start, end).months
  inline val years get() = Years.yearsBetween(start, end).years

  constructor(start: DateTime, minutesRange: Int) : this(start, start + minutesRange.minutes)
}

infix fun DateTime.rangeTo(end: DateTime): DateTimeRange {
  return DateTimeRange(this, end)
}
