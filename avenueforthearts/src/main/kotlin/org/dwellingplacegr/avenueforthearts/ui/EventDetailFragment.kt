package org.dwellingplacegr.avenueforthearts.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.*
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.squareup.moshi.Moshi
import org.dwellingplacegr.avenueforthearts.R
import org.dwellingplacegr.avenueforthearts.ext.load
import org.dwellingplacegr.avenueforthearts.http.Event
import org.dwellingplacegr.avenueforthearts.injection.App
import javax.inject.Inject
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.MarkerOptions
import android.content.Intent
import android.location.Geocoder
import android.net.Uri
import android.provider.CalendarContract
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CollapsingToolbarLayout
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import org.dwellingplacegr.avenueforthearts.ext.android.isGone
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import timber.log.Timber


class EventDetailFragment: Fragment(), OnMapReadyCallback {

  companion object {
    const val ARG_EVENT = "event"
  }

  @Inject lateinit var moshi: Moshi
  private val dateFormatter by lazy { DateTimeFormat.forPattern(getString(R.string.month_day_format)) }
  private val timeFormatter by lazy { DateTimeFormat.shortTime() }

  private lateinit var appBar: AppBarLayout
  private lateinit var mapContainer: View
  private lateinit var map: SupportMapFragment
  private lateinit var title: TextView
  private lateinit var description: TextView
  private lateinit var location: TextView
  private lateinit var dateAndTime: TextView

  private val event: Event by lazy {
    val json = arguments?.getString(ARG_EVENT)!!
    moshi.load<Event>(json)
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val view = inflater.inflate(R.layout.fragment_event_detail, container, false)

    App.graph.inject(this)

    val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
    toolbar.title = " "
    setHasOptionsMenu(true)

    // Set action bar on activity
    val activity = activity as? AppCompatActivity
    activity?.setSupportActionBar(toolbar)
    activity?.supportActionBar?.setDisplayHomeAsUpEnabled(true)

    return view
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return when (item.itemId) {
      android.R.id.home -> {
        activity?.finish()
        true
      }
      else -> { false }
    }
  }

  override fun onResume() {
    super.onResume()

    bindViews(view!!)

    this.title.text = event.name
    this.description.text = event.description.replace("\\s+", " ")
    this.location.text = event.place.name

    this.dateAndTime.text = getDateString(event.startTime, event.endTime)

    val backdrop = view?.findViewById<ImageView>(R.id.backdrop) ?: return
    val cover = event.cover

    if (cover != null) {
      Picasso.with(activity)
        .load(cover.source)
        .into(backdrop)
    } else {
      backdrop.isGone = true
      appBar.setExpanded(false)
    }

    val collapsingToolbarLayout = view!!.findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarLayout)
    appBar.addOnOffsetChangedListener(object: AppBarLayout.OnOffsetChangedListener {
      var isShow = false
      var scrollRange = -1

      override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
        if (scrollRange == -1) {
          scrollRange = appBar.totalScrollRange
        }
        if (scrollRange + verticalOffset == 0) {
          collapsingToolbarLayout.title = getString(R.string.title_event_detail)
          isShow = true
        } else if (isShow) {
          collapsingToolbarLayout.title = " "
          isShow = false
        }
      }
    })
  }

  override fun onMapReady(map: GoogleMap) {
    val location = event.place.location
    val placemark = if (location != null) {
      LatLng(location.latitude, location.longitude)
    } else {
      try {
        val geocoder = Geocoder(context)
        val address = geocoder.getFromLocationName(event.place.name, 1).firstOrNull()
        address?.let { LatLng(it.latitude, it.longitude) }
      } catch (e: Exception) {
        // Just in case
        Timber.e(e)
        null
      }
    }

    if (placemark == null) {
      Timber.e("Could not geocode '${event.place.name}'")
      return
    }

    mapContainer.isGone = false
    map.uiSettings.setAllGesturesEnabled(false)

    val marker = MarkerOptions().position(placemark).title(event.place.name)
    map.addMarker(marker)
    val cameraPosition = CameraPosition.builder()
      .target(placemark)
      .zoom(16f)
      .build()
    map.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

    map.setOnMapClickListener { _ ->
      val uri = Uri.parse("geo:${placemark.latitude},${placemark.longitude}?z=17&q=${marker.title}")
      val mapIntent = Intent(Intent.ACTION_VIEW, uri)
      startActivity(mapIntent)
    }
  }

  private fun bindViews(container: View) {
    this.appBar = container.findViewById(R.id.appbar)
    this.title = container.findViewById(R.id.title)
    this.description = container.findViewById(R.id.description)
    this.location = container.findViewById(R.id.location)
    this.dateAndTime = container.findViewById(R.id.dateAndTime)

    this.mapContainer = container.findViewById(R.id.mapContainer)
    this.map = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
    this.map.getMapAsync(this)

    val calendarButton = container.findViewById<View>(R.id.addToCalendar)
    calendarButton.setOnClickListener { addEventToCalendar() }
  }

  private fun getDateString(start: DateTime, end: DateTime?): String {
    val startDateStr = dateFormatter.print(start)
    val startTimeStr = timeFormatter.print(start)
    val startStr = getString(R.string.long_datetime_format, startDateStr, startTimeStr)

    val endStr = end?.let { end ->
      val endTimeStr = timeFormatter.print(end)
      if (start.withTimeAtStartOfDay() == end.withTimeAtStartOfDay()) {
        endTimeStr
      } else {
        val endDateStr = dateFormatter.print(end)
        getString(R.string.long_datetime_format, endDateStr, endTimeStr)
      }
    }

    return if (endStr != null) {
      getString(R.string.time_range_format, startStr, endStr)
    } else {
      startStr
    }
  }

  private fun getTimeString(start: DateTime, end: DateTime): String {
    val startTimeStr = timeFormatter.print(start)
    val endTimeStr = timeFormatter.print(end)
    return getString(R.string.time_range_format, startTimeStr, endTimeStr)
  }

  private fun addEventToCalendar() {
    val intent = Intent(Intent.ACTION_INSERT)
      .setData(CalendarContract.Events.CONTENT_URI)
      .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, event.startTime.millis)
      .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, event.endTime.millis)
      .putExtra(CalendarContract.Events.TITLE, event.name)
      .putExtra(CalendarContract.Events.DESCRIPTION, event.description)
      .putExtra(CalendarContract.Events.EVENT_LOCATION, event.place.name)
      .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY)
    startActivity(intent)
  }

  private fun shareEvent() {

  }
}
