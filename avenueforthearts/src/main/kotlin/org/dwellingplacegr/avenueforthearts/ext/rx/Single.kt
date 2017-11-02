package org.dwellingplacegr.avenueforthearts.ext.rx

import io.reactivex.Single
import io.reactivex.SingleSource
import io.reactivex.functions.BiFunction

/**
 * Zip two [Single] into a [Single] of a [Pair]
 */
fun <A, B> zipPair(first: SingleSource<A>, second: SingleSource<B>): Single<Pair<A, B>> {
  return Single.zip(first, second, BiFunction { a, b -> a to b })
}
