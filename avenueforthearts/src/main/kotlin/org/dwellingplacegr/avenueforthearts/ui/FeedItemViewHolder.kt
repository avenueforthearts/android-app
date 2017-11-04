package org.dwellingplacegr.avenueforthearts.ui

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import org.dwellingplacegr.avenueforthearts.R


class FeedItemViewHolder(view: View): RecyclerView.ViewHolder(view) {
  val card = view.findViewById<View>(R.id.card)
  val title = view.findViewById<TextView>(R.id.title)
  val description = view.findViewById<TextView>(R.id.description)
  val location = view.findViewById<TextView>(R.id.location)
  val dateAndTime = view.findViewById<TextView>(R.id.dateAndTime)
}
