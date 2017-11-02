@file:Suppress("NOTHING_TO_INLINE")

package org.dwellingplacegr.avenueforthearts.ext

import kotlin.jvm.internal.CallableReference
import kotlin.reflect.KClass
import kotlin.reflect.KDeclarationContainer
import kotlin.reflect.KProperty

inline val KProperty<*>.owner get() = (this as? CallableReference)?.owner
inline val KDeclarationContainer.canonicalName get() = (this as? KClass<*>)?.java?.canonicalName
inline val KProperty<*>.defaultDelegateName get() = owner?.canonicalName?.let { "$it::$name" } ?: name

interface ReadablePropertyDelegate<in This, out T> {
  operator fun getValue(thisRef: This, property: KProperty<*>): T
}

object CanonicalNameDelegate: ReadablePropertyDelegate<Any, String> {
  override operator fun getValue(thisRef: Any, property: KProperty<*>): String {
    return property.defaultDelegateName
  }
}

inline fun canonicalName(): ReadablePropertyDelegate<Any, String> = CanonicalNameDelegate
