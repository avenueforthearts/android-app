package org.dwellingplacegr.avenueforthearts.ext.rx

import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.FlowableEmitter

fun <T> createFlowable(
  mode: BackpressureStrategy,
  source: (FlowableEmitter<T>) -> Unit
): Flowable<T> { return Flowable.create(source, mode) }
