package org.dwellingplacegr.avenueforthearts.ui

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import org.dwellingplacegr.avenueforthearts.R
import org.dwellingplacegr.avenueforthearts.http.Event


class FeedAdapter(
  private val items: List<Event>,
  private val onClick: (Event) -> Unit
): RecyclerView.Adapter<FeedItemViewHolder>() {
//  val dateFormatter

  override fun getItemCount(): Int {
    return items.size
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedItemViewHolder {
    val inflater = LayoutInflater.from(parent.context)
    val view = inflater.inflate(R.layout.feed_item, parent, false)
    return FeedItemViewHolder(view)
  }

  override fun onBindViewHolder(holder: FeedItemViewHolder, position: Int) {
    val event = items[holder.adapterPosition]
    holder.title.text = event.name
    holder.card.setOnClickListener { onClick(event) }
  }
}
