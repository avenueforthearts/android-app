package org.dwellingplacegr.avenueforthearts.injection

import android.content.Context
import android.os.StrictMode
import android.support.multidex.MultiDex
import android.support.multidex.MultiDexApplication
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric
import org.dwellingplacegr.avenueforthearts.BuildConfig
import net.danlew.android.joda.JodaTimeAndroid
import timber.log.Timber

class App : MultiDexApplication() {

  companion object {
    @JvmStatic lateinit var graph: AppComponent
    @JvmStatic lateinit var instance: App
  }

  init {
    instance = this
  }

  override fun attachBaseContext(base: Context?) {
    super.attachBaseContext(base)
    MultiDex.install(this)
  }

  override fun onCreate() {
    super.onCreate()

    graph = DaggerAppComponent.builder()
        .androidModule(AndroidModule(this))
        .build()
    graph.inject(this)

    Fabric.with(applicationContext, Crashlytics())

    Timber.plant(Timber.DebugTree())

    JodaTimeAndroid.init(this)

    if (BuildConfig.DEBUG) {
      StrictMode.setThreadPolicy(
          StrictMode.ThreadPolicy.Builder()
              .detectAll()
              .penaltyDeath()
              .build()
      )
      StrictMode.setVmPolicy(
          StrictMode.VmPolicy.Builder()
              .detectAll()
              .penaltyLog()
              .build()
      )
    }
  }
}
