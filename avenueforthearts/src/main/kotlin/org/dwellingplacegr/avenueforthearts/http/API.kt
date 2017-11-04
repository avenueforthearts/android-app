package org.dwellingplacegr.avenueforthearts.http

import com.squareup.moshi.Json
import com.squareup.moshi.Moshi
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.dwellingplacegr.avenueforthearts.BuildConfig
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import timber.log.Timber


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
      retrofit.create(Feed::class.java)
    }
  }

  interface Feed {
    @GET("events.json")
    fun getFeed(): Single<List<Event>>
  }
}

data class Event(
  val name: String
)