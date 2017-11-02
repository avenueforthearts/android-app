package org.dwellingplacegr.avenueforthearts.ext.android

import android.view.View
import android.view.View.*

inline var View.isGone: Boolean
  get() { return visibility == GONE }
  set(gone) { visibility = if (gone) GONE else VISIBLE }

inline var View.isInvisible: Boolean
  get() { return visibility == INVISIBLE }
  set(invisible) { visibility = if (invisible) INVISIBLE else VISIBLE }

inline var View.isVisible: Boolean
  get() { return visibility == VISIBLE }
  set(visible) { visibility = if (visible) VISIBLE else INVISIBLE }
