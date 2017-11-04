package org.dwellingplacegr.avenueforthearts.injection

import dagger.Module
import dagger.Provides
import org.dwellingplacegr.avenueforthearts.http.API
import javax.inject.Named

@Module
class HttpModule {

  @Provides
  fun provideApiClient(): API.Client {
    return API.Client()
  }
}
