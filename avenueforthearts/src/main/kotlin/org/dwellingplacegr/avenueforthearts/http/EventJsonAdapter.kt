package org.dwellingplacegr.avenueforthearts.http

import com.squareup.moshi.FromJson
import com.squareup.moshi.Json
import com.squareup.moshi.ToJson
import org.joda.time.DateTime

class EventJsonAdapter {
    @FromJson
    fun eventFromJson(event: Event): Event {
        return event
    }
}

data class Container(
    val data: List<Event>
)

data class Event(
    val name: String,
    val description: String,
    val id: String,
    val place: Place,

    @Json(name="end_time")
    val endTime: DateTime,

    @Json(name="start_time")
    val startTime: DateTime
)

data class Place(
    val name: String,
    val location: Location,
    val id: String
)

data class Location (
    val city: String,
    val country: String,
    val latitude: Double,
    val longitude: Double,
    val state: String,
    val street: String,
    val zip: String //Should be int?
)