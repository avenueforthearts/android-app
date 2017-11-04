package org.dwellingplacegr.avenueforthearts.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.squareup.moshi.Moshi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import org.dwellingplacegr.avenueforthearts.R
import org.dwellingplacegr.avenueforthearts.ext.android.isGone
import org.dwellingplacegr.avenueforthearts.ext.dump
import org.dwellingplacegr.avenueforthearts.http.API
import org.dwellingplacegr.avenueforthearts.http.Event
import org.dwellingplacegr.avenueforthearts.injection.App
import org.dwellingplacegr.avenueforthearts.ui.EventDetailFragment.Companion.ARG_EVENT
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.annotations.Nls
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject


class FeedFragment: Fragment() {

  @Inject lateinit var moshi: Moshi
  @Inject lateinit var client: API.Client

  private lateinit var swipeRefresh: SwipeRefreshLayout
  private lateinit var recycler: RecyclerView
  private lateinit var emptyView: View
  private lateinit var emptyHeading: TextView
  private lateinit var emptySubheading: TextView
  private var susbscribers = CompositeDisposable()

  private var adapter = FeedAdapter(emptyList(), this::onEventSelected)
    set(value) {
      this.recycler.adapter = value
    }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val view = inflater.inflate(R.layout.fragment_feed, container, false)
    retainInstance = true

    App.graph.inject(this)

    this.bindViews(view)
    showEmptyMessage(
      getString(R.string.checking_out_whats_happening),
      getString(R.string.sit_tight)
    )
    this.refreshEvents()

    return view
  }

  override fun onDestroyView() {
    susbscribers.clear()
    super.onDestroyView()
  }

  private fun bindViews(container: View) {
    this.swipeRefresh = container.findViewById(R.id.swipeRefresh)
    this.swipeRefresh.onRefresh {
      refreshEvents()
    }
    this.recycler = container.findViewById(R.id.feedList)
    this.emptyView = container.findViewById(R.id.emptyView)
    this.emptyHeading = container.findViewById(R.id.emptyHeading)
    this.emptySubheading = container.findViewById(R.id.emptySubheading)
  }

  private fun refreshEvents() {
    this.swipeRefresh.isRefreshing = true
    this.client.feed.getFeed()
      .observeOn(AndroidSchedulers.mainThread())
      .subscribeBy(
        onSuccess = { events ->
          swipeRefresh.isRefreshing = false
          isEmptyMessageHidden = true
          this.adapter = FeedAdapter(events, this::onEventSelected)
          Timber.d("Events! $events")
        },
        onError = { error ->
          swipeRefresh.isRefreshing = false

          val message = when (error) {
            is IOException -> {
              getString(R.string.error_check_network_connection)
            }
            else -> {
              Timber.e("Error loading saved searches: ${error.message}")
              getString(R.string.error_swipe_to_reload)
            }
          }
          showEmptyMessage(
            getString(R.string.error_downloading_events),
            message
          )

          Timber.e(error, "Failed to get events!")
        }
      )
      .addTo(this.susbscribers)
  }

  private fun onEventSelected(event: Event) {
    Timber.d("Clicked event $event")
    val eventJson = moshi.dump(event)
    startActivity<EventDetailActivity>(ARG_EVENT to eventJson)
  }

  private var isEmptyMessageHidden = true
    set(hidden) {
      val duration = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()
      emptyView.isGone = hidden
      recycler.isGone = !hidden
      emptyView.animate()
        .setDuration(duration)
        .alpha(if (hidden) 0.0f else 1.0F)
      recycler.isGone = !hidden
      recycler.animate()
        .setDuration(duration)
        .alpha(if (hidden) 1.0f else 0.0F)
    }

  private fun showEmptyMessage(@Nls heading: String, @Nls subHeading: String? = null) {
    emptyHeading.text = heading
    emptySubheading.text = subHeading
    isEmptyMessageHidden = false
  }
}
