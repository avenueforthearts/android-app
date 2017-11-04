package org.dwellingplacegr.avenueforthearts.sync

import android.accounts.Account
import android.content.AbstractThreadedSyncAdapter
import android.content.ContentProviderClient
import android.content.Context
import android.content.SyncResult
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import org.dwellingplacegr.avenueforthearts.http.API
import org.dwellingplacegr.avenueforthearts.injection.App
import timber.log.Timber
import javax.inject.Inject


class SyncAdapter(context: Context, autoInitialize: Boolean): AbstractThreadedSyncAdapter(context, autoInitialize) {

  @Inject lateinit var client: API.Client

  init {
    App.graph.inject(this)
  }

  override fun onPerformSync(
    account: Account,
    extras: Bundle,
    authority: String,
    contentProviderClient: ContentProviderClient,
    syncResult: SyncResult
  ) {
    Handler(Looper.getMainLooper()).post {
      try {
        Timber.i("Periodic sync: start")

        val events = client.feed.getFeed()
          .blockingGet()

        Timber.i("Events: $events")

      } catch (e: Exception) {
        Timber.e(e, "onPerformSync() failure")
      }
    }
  }
}
