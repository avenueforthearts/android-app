package org.dwellingplacegr.avenueforthearts.sync

import android.app.Service
import android.content.Intent
import android.os.IBinder

/**
 * Define a Service that returns an IBinder for the
 * sync adapter class, allowing the sync adapter framework to call
 * onPerformSync().
 */
class SyncService: Service() {

  /**
   * Return an object that allows the system to invoke
   * the sync adapter.
   *
   */
  override fun onBind(intent: Intent): IBinder? {
    /*
     * Get the object that allows external processes
     * to call onPerformSync(). The object is created
     * in the base class code when the ChurchSyncAdapter
     * constructors call super()
     */
    return SyncAdapter(applicationContext, true).syncAdapterBinder
  }
}
