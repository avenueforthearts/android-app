package org.dwellingplacegr.avenueforthearts.http

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import org.joda.time.DateTime

@Suppress("unused")
class TimestampConversion {

    @FromJson
    //2017-10-02T16:00:00-0400
    fun fromJson(json: String): DateTime {
        return DateTime.parse(json)
    }

    @ToJson
    fun toJson(date: DateTime): String {
        return date.toString()
    }
}
