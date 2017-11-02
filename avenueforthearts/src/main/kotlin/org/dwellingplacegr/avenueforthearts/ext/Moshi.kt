package org.dwellingplacegr.avenueforthearts.ext

import com.squareup.moshi.Moshi

inline fun <reified T: Any> Moshi.dump(obj: T): String {
  return this.adapter(T::class.java).toJson(obj)
}

inline fun <reified T: Any> Moshi.load(json: String): T {
  return this.adapter(T::class.java).fromJson(json)!!
}
