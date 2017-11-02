package org.dwellingplacegr.avenueforthearts.injection

import android.accounts.AccountManager
import android.app.Application
import android.content.Context
import android.content.pm.PackageInfo
import org.dwellingplacegr.avenueforthearts.injection.scope.ForApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import org.jetbrains.anko.accountManager

/**
 * A module for Android-specific dependencies which require a
 * [android.content.Context] or [ ] to create.
 */
@Module
class AndroidModule(private val application: Application) {

  /**
   * Allow the application context to be injected but require that it be
   * annotated with [ ][ForApplication] to explicitly differentiate it from
   * an activity context.
   */
  @Provides
  @Singleton
  @ForApplication
  fun provideApplicationContext(): Context {
    return application
  }

  @Provides
  fun providePackageInfo(@ForApplication context: Context): PackageInfo {
    return context.packageManager.getPackageInfo(context.packageName, 0)
  }

  @Provides
  fun provideAccountManager(@ForApplication context: Context): AccountManager {
    return context.accountManager
  }
}
