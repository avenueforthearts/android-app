package org.dwellingplacegr.avenueforthearts.injection

import org.dwellingplacegr.avenueforthearts.ui.MainActivity
import dagger.Component
import javax.inject.Singleton

/**
 * Defines the injection interface for dagger.
 * The dagger compiled version of this class is prefixed with "Dagger"
 */
@Singleton
@Component(
  modules = arrayOf(
    AndroidModule::class,
    HttpModule::class,
    AppModule::class
  )
)
interface AppComponent {
  fun inject(application: App)
  fun inject(activity: MainActivity)

}
