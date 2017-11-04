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
import android.net.Uri
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CoordinatorLayout
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_event_detail.*
import org.dwellingplacegr.avenueforthearts.BuildConfig
import org.dwellingplacegr.avenueforthearts.ext.android.isGone


class EventDetailFragment: Fragment(), OnMapReadyCallback {

  companion object {
    const val ARG_EVENT = "event"
  }

  @Inject lateinit var moshi: Moshi

  private lateinit var appBar: AppBarLayout
  private lateinit var mapContainer: View
  private lateinit var map: SupportMapFragment
  private lateinit var heading: TextView
  private lateinit var subheading: TextView

  private val event: Event by lazy {
    val json = arguments?.getString(ARG_EVENT)!!
    moshi.load<Event>(json)
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val view = inflater.inflate(R.layout.fragment_event_detail, container, false)

    App.graph.inject(this)

    val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
    toolbar.title = event.name
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

    this.heading.text = event.name
    this.subheading.text = event.description.replace("\\s+", " ")

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
  }

  override fun onMapReady(map: GoogleMap) {
    val location = event.place.location ?: return
    map.uiSettings.setAllGesturesEnabled(false)

    val placemark = LatLng(location.latitude, location.longitude)
    val marker = MarkerOptions().position(placemark).title(event.place.name)
    map.addMarker(marker)
    val cameraPosition = CameraPosition.builder()
      .target(placemark)
      .zoom(17f)
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
    this.heading = container.findViewById(R.id.head)
    this.subheading = container.findViewById(R.id.sub)

    this.mapContainer = container.findViewById(R.id.mapContainer)
    if (this.event.place.location?.latitude != null) {
      this.map = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
      this.map.getMapAsync(this)
      this.mapContainer.isGone = false
    } else {
      this.mapContainer.isGone = true
    }
  }
}
