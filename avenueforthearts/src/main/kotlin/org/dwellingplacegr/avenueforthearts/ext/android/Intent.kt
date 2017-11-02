package org.dwellingplacegr.avenueforthearts.ext.android

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import java.io.Serializable
import org.jetbrains.anko.AnkoException

/**
 * Add extended data to the intent.
 *
 * @param extras Pairs to set as extras on the intent.
 */
fun Intent.putExtras(vararg extras: Pair<String, Any>) {
  extras.forEach { putExtra(it) }
}

/**
 * Add extended data to the intent.
 *
 * @param extra Pair to set as an extra on the intent.
 */
fun Intent.putExtra(extra: Pair<String, Any>) {
  val (k, v) = extra
  when (v) {
    is Boolean -> putExtra(k, v)
    is Byte -> putExtra(k, v)
    is Char -> putExtra(k, v)
    is Short -> putExtra(k, v)
    is Int -> putExtra(k, v)
    is Long -> putExtra(k, v)
    is Float -> putExtra(k, v)
    is Double -> putExtra(k, v)
    is String -> putExtra(k, v)
    is CharSequence -> putExtra(k, v)
    is Parcelable -> putExtra(k, v)
    is Serializable -> putExtra(k, v)
    is BooleanArray -> putExtra(k, v)
    is ByteArray -> putExtra(k, v)
    is CharArray -> putExtra(k, v)
    is DoubleArray -> putExtra(k, v)
    is FloatArray -> putExtra(k, v)
    is IntArray -> putExtra(k, v)
    is LongArray -> putExtra(k, v)
    is Array<*> -> {
      @Suppress("UNCHECKED_CAST")
      when {
        v.isArrayOf<Parcelable>() -> putExtra(k, v as Array<out Parcelable>)
        v.isArrayOf<CharSequence>() -> putExtra(k, v as Array<out CharSequence>)
        v.isArrayOf<String>() -> putExtra(k, v as Array<out String>)
        else -> throw AnkoException("Unsupported bundle component (${v.javaClass})")
      }
    }
    is ShortArray -> putExtra(k, v)
    is Bundle -> putExtra(k, v)
    else -> throw AnkoException("Unsupported intent extra (${v.javaClass})")
  }
}

/**
 * Create an intent with extended data.
 *
 * @param extras Extended data items to add to the intent.
 */
fun intentWith(vararg extras: Pair<String, Any>): Intent {
  val intent = Intent()
  intent.putExtras(*extras)
  return intent
}

operator fun Intent.contains(key: String): Boolean = hasExtra(key)
