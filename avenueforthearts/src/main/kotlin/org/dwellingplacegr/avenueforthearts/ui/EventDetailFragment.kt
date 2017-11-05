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
import org.dwellingplacegr.avenueforthearts.ext.isSameDayAs
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
    if (event.description.isNullOrEmpty()) {
      this.description.isGone = true
    } else {
      this.description.isGone = false
      this.description.text = event.description?.replace("\\s+", " ")
    }
    this.location.text = event.placeName

    this.dateAndTime.text = formatEventDate(event)

    val backdrop = view?.findViewById<ImageView>(R.id.backdrop) ?: return
    val cover = event.cover

    if (cover.isNullOrEmpty()) {
      backdrop.isGone = true
      appBar.setExpanded(false)
    } else {
      Picasso.with(activity)
        .load(cover)
        .into(backdrop)
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
    val location = event.location
    val placemark = location ?: {
      try {
        val geocoder = Geocoder(context)
        val address = geocoder.getFromLocationName(event.placeName, 1).firstOrNull()
        address?.let { LatLng(it.latitude, it.longitude) }
      } catch (e: Exception) {
        // Just in case
        Timber.e(e)
        null
      }
    }()

    if (placemark == null) {
      Timber.e("Could not geocode '${event.placeName}'")
      return
    }

    mapContainer.isGone = false
    map.uiSettings.setAllGesturesEnabled(false)

    val marker = MarkerOptions().position(placemark).title(event.placeName)
    map.addMarker(marker)
    val zoom = 16f
    val cameraPosition = CameraPosition.builder()
      .target(placemark)
      .zoom(zoom)
      .build()
    map.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

    map.setOnMapClickListener { _ ->
      val uri = Uri.parse("geo:${placemark.latitude},${placemark.longitude}?z=$zoom&q=${marker.title}")
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

    val openInFacebook = container.findViewById<View>(R.id.visitFbPage)
    openInFacebook.setOnClickListener { openInFacebook() }

    val shareEvent = container.findViewById<View>(R.id.shareEvent)
    shareEvent.setOnClickListener { shareEvent() }
  }

  private fun formatEventDate(event: Event): String {
    val startDateStr = dateFormatter.print(event.startTime)
    val startTimeStr = timeFormatter.print(event.endTime)
    val startStr = getString(R.string.long_datetime_format, startDateStr, startTimeStr)

    val endStr = event.endTime?.let { end ->
      val endTimeStr = timeFormatter.print(end)
      if (event.startTime.isSameDayAs(end)) {
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

  private fun addEventToCalendar() {
    val intent = Intent(Intent.ACTION_INSERT)
      .setData(CalendarContract.Events.CONTENT_URI)
      .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, event.startTime.millis)
      .putExtra(CalendarContract.Events.TITLE, event.name)
      .putExtra(CalendarContract.Events.DESCRIPTION, event.description)
      .putExtra(CalendarContract.Events.EVENT_LOCATION, event.placeName)
      .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY)

    event.endTime?.let { end ->
      // TODO does calendar app allow this????
      intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, end.millis)
    }
    startActivity(intent)
  }

  private fun openInFacebook() {
    val intent = Intent(Intent.ACTION_VIEW)
    intent.data = activity?.packageManager?.let { pm ->
      try {
        // Check if we can open in facebook app
        // https://stackoverflow.com/a/34564284/1255482
        val pi = pm.getPackageInfo("com.facebook.katana", 0)
        val ai = pm.getApplicationInfo("com.facebook.katana", 0)
        if (ai.enabled && pi.versionCode > 3002850) {
          Uri.parse("fb://facewebmodal/f?href=${event.url}")
        } else {
          event.url
        }
      } catch (e: Exception) {
        event.url
      }
    } ?: event.url
    startActivity(intent)
  }

  private fun shareEvent() {
    val sharingIntent = Intent(Intent.ACTION_SEND)
    sharingIntent.type = "text/plain"
    val nameAndLocation = getString(R.string.share_name_loc, event.name, event.placeName)
    sharingIntent.putExtra(Intent.EXTRA_SUBJECT, nameAndLocation)
    val descAndLink = getString(
      R.string.share_text,
      event.name,
      event.placeName,
      formatEventDate(event),
      event.friendlyUrl
    )
    sharingIntent.putExtra(Intent.EXTRA_TEXT, descAndLink)
    startActivity(Intent.createChooser(sharingIntent, getString(R.string.share_with)))
  }
}
