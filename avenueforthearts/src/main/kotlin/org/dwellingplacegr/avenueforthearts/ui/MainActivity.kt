package org.dwellingplacegr.avenueforthearts.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import org.dwellingplacegr.avenueforthearts.R


class MainActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContentView(R.layout.activity_main)

    if (savedInstanceState == null) {
      val fragment = FeedFragment()
      supportFragmentManager.beginTransaction()
        .add(R.id.event_list_container, fragment)
        .commit()
    }
  }
}
