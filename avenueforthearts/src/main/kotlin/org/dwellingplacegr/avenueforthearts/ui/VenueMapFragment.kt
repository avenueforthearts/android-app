package org.dwellingplacegr.avenueforthearts.ui

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import org.dwellingplacegr.avenueforthearts.BuildConfig
import org.dwellingplacegr.avenueforthearts.R
import org.dwellingplacegr.avenueforthearts.R.id.webview
import org.jetbrains.anko.bundleOf


class VenueMapFragment: Fragment() {
  private lateinit var webview: WebView

  @SuppressLint("SetJavaScriptEnabled")
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, state: Bundle?): View? {
    val view = inflater.inflate(R.layout.fragment_venue_map, container, false)
    retainInstance = true

    bindViews(view)

    if (state == null) {
      webview.loadUrl(BuildConfig.VENUE_MAP_URL)
      webview.settings.javaScriptEnabled = true
      webview.webViewClient = object: WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
          // Prevent opening google maps app
          return if (request.url == Uri.parse(BuildConfig.VENUE_MAP_URL)) {
            false
          } else {
            super.shouldOverrideUrlLoading(view, request)
          }
        }
      }
    } else {
      webview.restoreState(state)
    }

    return view
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    webview.saveState(outState)
  }

  private fun bindViews(container: View) {
    this.webview = container.findViewById(R.id.webview)
  }
}
