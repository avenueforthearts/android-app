package org.dwellingplacegr.avenueforthearts.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import com.google.android.gms.maps.SupportMapFragment
import org.dwellingplacegr.avenueforthearts.R

class EventDetailActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContentView(R.layout.activity_event_detail)

    if (savedInstanceState == null) {
      val fragment = EventDetailFragment()
      fragment.arguments = intent.extras
      supportFragmentManager.beginTransaction()
        .add(R.id.event_detail_container, fragment)
        .commit()
    }
  }
}
