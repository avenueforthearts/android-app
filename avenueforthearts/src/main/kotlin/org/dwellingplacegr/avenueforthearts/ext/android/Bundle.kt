package org.dwellingplacegr.avenueforthearts.ext.android

import android.os.Bundle

operator fun Bundle.contains(key: String): Boolean = containsKey(key)
