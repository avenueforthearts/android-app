package org.dwellingplacegr.avenueforthearts.sync

import android.app.Service
import android.content.Intent
import android.os.IBinder

class AccountAuthenticatorService : Service() {

  private lateinit var mAccountAuthenticator: AccountAuthenticator

  override fun onCreate() {
    mAccountAuthenticator = AccountAuthenticator(this)
  }

  /*
   * When the system binds to this Service to make the RPC call return the authenticator's
   * IBinder.
   */
  override fun onBind(intent: Intent): IBinder? {
    return mAccountAuthenticator.iBinder
  }
}
