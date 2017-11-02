package org.dwellingplacegr.avenueforthearts.injection

import android.content.pm.PackageInfo
import android.os.Build
import org.dwellingplacegr.avenueforthearts.BuildConfig
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module
class HttpModule {

  @Provides
  @Singleton
  @Named("userAgent")
  fun provideUserAgentString(packageInfo: PackageInfo): String {
    return String.format(
      "%s/%s (Android %s; Build %s; API %s; %s %s/%s %s)",
      BuildConfig.APPLICATION_ID,
      packageInfo.versionName,
      Build.VERSION.RELEASE,
      Build.ID,
      Build.VERSION.SDK_INT,
      Build.MANUFACTURER,
      Build.DEVICE,
      Build.BRAND,
      Build.MODEL
    )
  }
}
