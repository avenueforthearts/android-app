package org.dwellingplacegr.avenueforthearts.injection

import android.content.Context
import android.support.multidex.MultiDex
import android.support.multidex.MultiDexApplication
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric
import net.danlew.android.joda.JodaTimeAndroid
import timber.log.BuildConfig
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

    @Suppress("ConstantConditionIf")
    if (!BuildConfig.DEBUG) {
      // Dont spam crashlytics with dev crashes
      Fabric.with(applicationContext, Crashlytics())
    }

    Timber.plant(Timber.DebugTree())

    JodaTimeAndroid.init(this)

//    SyncHelper.initializePeriodicSync(this)
  }
}
