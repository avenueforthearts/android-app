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
import android.widget.ImageView
import com.squareup.picasso.Picasso
import org.dwellingplacegr.avenueforthearts.BuildConfig


class EventDetailFragment: Fragment(), OnMapReadyCallback {
  private lateinit var map: SupportMapFragment

  companion object {
    const val ARG_EVENT = "event"
  }

  @Inject lateinit var moshi: Moshi

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

    this.map = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
    this.map.getMapAsync(this)

    val backdrop = view?.findViewById<ImageView>(R.id.backdrop)
    Picasso.with(activity)
      .load("https://scontent-ort2-1.xx.fbcdn.net/v/t1.0-9/22089969_1547046205339337_1393822853490504093_n.jpg?oh=0f1256e57b7732764a579ce900342c00&oe=5AAAD025")
      .into(backdrop)
  }

  override fun onMapReady(map: GoogleMap) {
    map.uiSettings.setAllGesturesEnabled(false)
    val placemark = LatLng(42.959852, -85.667677)
    val marker = MarkerOptions().position(placemark).title("Sanctuary Folk Art Gallery")
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
}
