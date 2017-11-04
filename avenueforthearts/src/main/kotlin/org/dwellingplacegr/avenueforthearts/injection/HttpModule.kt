package org.dwellingplacegr.avenueforthearts.injection

import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import org.dwellingplacegr.avenueforthearts.http.API
import javax.inject.Named

@Module
class HttpModule {
  @Provides
  fun provideMoshi(): Moshi {
    return Moshi.Builder().build()
  }

  @Provides
  fun provideApiClient(moshi: Moshi): API.Client {
    return API.Client(moshi)
  }
}
