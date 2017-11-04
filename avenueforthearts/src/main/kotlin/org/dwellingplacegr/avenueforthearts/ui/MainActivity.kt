package org.dwellingplacegr.avenueforthearts.ui

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import org.dwellingplacegr.avenueforthearts.R


class MainActivity : AppCompatActivity() {
  private lateinit var pager: ViewPager
  private lateinit var tabBar: BottomNavigationView

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContentView(R.layout.activity_main)

    pager = findViewById(R.id.pager)
    tabBar = findViewById(R.id.bottom_navigation)

    if (savedInstanceState == null) {
//      val fragment = FeedFragment()
//      supportFragmentManager.beginTransaction()
//        .add(R.id.event_list_container, fragment)
//        .commit()
      pager.adapter = TabsAdapter()
    }

    tabBar.setOnNavigationItemSelectedListener { item ->
      val index = when (item.itemId) {
        R.id.tabEvents -> 0
        else -> 1
      }
      pager.setCurrentItem(index, true)

      true
    }

    pager.addOnPageChangeListener(object: ViewPager.SimpleOnPageChangeListener() {
      override fun onPageSelected(position: Int) {
        tabBar.menu.getItem(position).isChecked = true
      }
    })
  }

  inner private class TabsAdapter: FragmentPagerAdapter(supportFragmentManager) {
    override fun getItem(position: Int) = when (position) {
      0 -> FeedFragment()
      else -> VenueMapFragment()
    }

    override fun getCount() = 2
  }
}
