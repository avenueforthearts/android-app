package org.dwellingplacegr.avenueforthearts.sync

import android.accounts.Account
import android.content.ContentResolver
import android.content.Context
import android.os.Bundle
import org.dwellingplacegr.avenueforthearts.R
import org.jetbrains.anko.accountManager

object SyncHelper {
  private const val ACCOUNT_NAME = "Sync"

  @JvmStatic fun initializePeriodicSync(context: Context) {
    val account = Account(ACCOUNT_NAME, context.getString(R.string.account_type))
    context.accountManager.addAccountExplicitly(account, null, null)
    ContentResolver.setSyncAutomatically(
      account,
      context.getString(R.string.content_authority),
      true
    )
    ContentResolver.addPeriodicSync(
      account,
      context.getString(R.string.content_authority),
      Bundle.EMPTY,
      (12 * 60 * 60).toLong()  // 12 hours
    )
  }
}
