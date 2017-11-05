package org.dwellingplacegr.avenueforthearts.http

import com.squareup.moshi.Moshi
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import okhttp3.CacheControl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.dwellingplacegr.avenueforthearts.BuildConfig
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import timber.log.Timber
import java.util.concurrent.TimeUnit


class API {
  class Client(private val moshi: Moshi) {
    private val okClient: OkHttpClient by lazy {
      val loggingInterceptor = HttpLoggingInterceptor(
        HttpLoggingInterceptor.Logger { message ->
          Timber.tag("Avenue").d(message)
        }
      )
      loggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC

      OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()
    }
    private val retrofit: Retrofit by lazy {
      val retrofitBuilder = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .client(okClient)
        .addCallAdapterFactory(
          RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io())
        )

      retrofitBuilder.addConverterFactory(
        MoshiConverterFactory.create(moshi)
      )

      retrofitBuilder.build()
    }

    val feed: Feed by lazy {
      Feed(retrofit.create(Feed.Endpoint::class.java))
    }
  }

  class Feed internal constructor(private val endpoint: Feed.Endpoint) {
    internal interface Endpoint {
      @GET("events")
      fun getFeed(
        @Header("Cache-Control") cacheControl: CacheControl?
      ): Single<List<Event>>
    }

    fun getFeed(forceCache: Boolean = false): Single<List<Event>> {
      val cc = if (forceCache) {
        CacheControl.Builder()
          .maxStale(Int.MAX_VALUE, TimeUnit.SECONDS)
          .onlyIfCached()
          .build()
      } else {
        null
      }
      return endpoint.getFeed(cacheControl = cc)
    }
  }
}
