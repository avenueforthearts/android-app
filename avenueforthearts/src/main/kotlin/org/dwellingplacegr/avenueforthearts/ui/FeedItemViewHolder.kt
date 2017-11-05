package org.dwellingplacegr.avenueforthearts.ui

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.afollestad.sectionedrecyclerview.SectionedViewHolder
import org.dwellingplacegr.avenueforthearts.R

class FeedItemViewHolder(view: View): SectionedViewHolder(view) {
  val card = view.findViewById<View>(R.id.card)
  val title = view.findViewById<TextView>(R.id.title)
  val description = view.findViewById<TextView>(R.id.description)
  val location = view.findViewById<TextView>(R.id.location)
  val dateAndTime = view.findViewById<TextView>(R.id.dateAndTime)
  val image = view.findViewById<ImageView>(R.id.coverImage)
}
