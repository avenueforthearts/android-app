package org.dwellingplacegr.avenueforthearts.http

import android.net.Uri
import com.google.android.gms.maps.model.LatLng
import com.squareup.moshi.Json
import org.joda.time.DateTime

data class Event(
  @Json(name = "event_name")
  val name: String,
  val description: String?,
  val id: String,
  @Json(name="start_time")
  val startTime: DateTime,
  @Json(name="end_time")
  val endTime: DateTime,

  @Json(name = "place_name")
  val placeName: String,

  val cover: String?,
  val category: String?,
  val owner: String?,
  @Json(name = "ticket_uri")
  val ticketUri: String?,

  val latitude: Double?,
  val longitude: Double?,
  val city: String?,
  val country: String?,
  val state: String?,
  val street: String?,
  val zip: String?
) {
  val url: Uri get() {
    return Uri.parse("https://facebook.com/events/$id")
  }
  val friendlyUrl: String get() {
    return "facebook.com/events/$id"
  }

  val location: LatLng? get() {
    return if (latitude != null && longitude != null) {
      LatLng(latitude, longitude)
    } else { null }
  }
}

/*
{
    "id": "146330839316331",
    "event_name": "Misc. Print Exchange and Exhibition",
    "place_name": "Bend Gallery",
    "start_time": "2017-12-01T23:00:00Z",
    "end_time": "2017-12-02T02:00:00Z",
    "description": "Misc. is a varied print portfolio exchange and exhibition. This show presents an international print exchange and exhibition organized by Bend Gallery which features the work of 12 artists. Each artist created a varied edition of 11 x 15 in. unique prints exploring the theme \"Misc.\" through a variety of printmaking techniques.\n\nA print from each artist will be on display for the reception of this exhibition. Featured artists include:\n\nE.J. Cobb\nElle Friedberg\nSom\u00e9 Louis\nKimberly Mills\nJames Nantelle\nAlison Palmateer\nAlan Pocaro\nErin Schaenzer\nJill Smith\nJosh Stutz\nMatt Squibb\nDayna Walton",
    "category": "EVENT_ART",
    "owner": "Bend Gallery",
    "ticket_uri": "",
    "cover": "https://scontent.xx.fbcdn.net/v/t31.0-8/s720x720/22548819_250677185456941_2974261595224508998_o.jpg?oh=321c4c7f53846b4c3dfa622aa9e11e36&oe=5A6450E5",
    "street": "40 Division Ave. S",
    "city": "Grand Rapids",
    "state": "MI",
    "zip": "49503",
    "country": "United States",
    "latitude": "42.96271960257899991120",
    "longitude": "-85.66593647003199407663"
}
 */
