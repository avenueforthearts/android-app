package org.dwellingplacegr.avenueforthearts.sync

import android.accounts.Account
import android.content.AbstractThreadedSyncAdapter
import android.content.ContentProviderClient
import android.content.Context
import android.content.SyncResult
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import timber.log.Timber


class SyncAdapter(context: Context, autoInitialize: Boolean): AbstractThreadedSyncAdapter(context, autoInitialize) {
//
//  init {
//    App.graph.inject(this)
//  }

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


      } catch (e: Exception) {
        Timber.e(e, "onPerformSync() failure")
      }
    }
  }
}
