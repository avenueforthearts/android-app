package org.dwellingplacegr.avenueforthearts.ui

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import io.reactivex.subjects.PublishSubject
import org.dwellingplacegr.avenueforthearts.R
import org.dwellingplacegr.avenueforthearts.ext.android.isGone
import org.dwellingplacegr.avenueforthearts.http.Event
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat


class FeedAdapter(
  private val context: Context,
  private val items: List<Event>
): RecyclerView.Adapter<FeedItemViewHolder>() {
  val dateFormatter = DateTimeFormat.forPattern(context.getString(R.string.month_day_format))
  val timeFormatter = DateTimeFormat.shortTime()

  val selections = PublishSubject.create<Event>()

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
    holder.location.text = event.place.name
    holder.description.text = cleanupDescription(event.description)
    holder.location.text = event.place.name
    holder.dateAndTime.text = getTimeString(event.startTime, event.endTime)
    holder.card.setOnClickListener { selections.onNext(event) }


    if (event.cover != null) {
      holder.image.isGone = false
      Picasso.with(context)
        .load(event.cover.source)
        .into(holder.image)
    } else {
      holder.image.isGone = true
    }
  }

  private fun cleanupDescription(desc: String): String {
    // Cleanup excess consecutive whitespaces
    return desc.replace("\\s+", " ")
  }

  private fun getTimeString(start: DateTime, end: DateTime?): String {
    val startDateStr = dateFormatter.print(start)
    val startTimeStr = timeFormatter.print(start)
    val startStr = context.getString(R.string.long_datetime_format, startDateStr, startTimeStr)

    val endStr = end?.let { end ->
      val endTimeStr = timeFormatter.print(end)
      if (start.withTimeAtStartOfDay() == end.withTimeAtStartOfDay()) {
        endTimeStr
      } else {
        val endDateStr = dateFormatter.print(end)
        context.getString(R.string.long_datetime_format, endDateStr, endTimeStr)
      }
    }

    return if (endStr != null) {
      context.getString(R.string.time_range_format, startStr, endStr)
    } else {
      startStr
    }
  }
}
