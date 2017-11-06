package org.dwellingplacegr.avenueforthearts.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.afollestad.sectionedrecyclerview.SectionedRecyclerViewAdapter
import com.squareup.picasso.Picasso
import io.reactivex.subjects.PublishSubject
import org.dwellingplacegr.avenueforthearts.R
import org.dwellingplacegr.avenueforthearts.ext.android.isGone
import org.dwellingplacegr.avenueforthearts.ext.isSameDayAs
import org.dwellingplacegr.avenueforthearts.http.Event
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat


class FeedAdapter(
  private val context: Context,
  private val items: List<Pair<Pair<String, DateTime?>, List<Event>>>
): SectionedRecyclerViewAdapter<FeedItemViewHolder>() {

  init {
    shouldShowHeadersForEmptySections(false)
  }

  override fun getItemCount(section: Int): Int {
    return eventsForSection(section).size
  }

  private fun eventsForSection(section: Int): List<Event> {
    val (_, events) = items[section]
    return events
  }

  override fun onBindViewHolder(holder: FeedItemViewHolder, section: Int, relativePosition: Int, absolutePosition: Int) {
    val event = eventsForSection(section)[relativePosition]
    holder.title.text = event.name
    holder.location.text = event.placeName
    if (event.description.isNullOrEmpty()) {
      holder.description.isGone = true
    } else {
      holder.description.text = cleanupDescription(event.description!!)
      holder.description.isGone = false
    }
    holder.dateAndTime.text = getTimeString(event.startTime, event.endTime)
    holder.card.setOnClickListener { selections.onNext(event) }

    if (!event.cover.isNullOrEmpty()) {
      holder.image.isGone = false
      Picasso.with(context)
        .load(event.cover)
        .error(R.drawable.placeholder)
        .into(holder.image)
    }
  }

  override fun onBindHeaderViewHolder(holder: FeedItemViewHolder, sectionIndex: Int, expanded: Boolean) {
    val (section, _) = items[sectionIndex]
    val (title, date) = section
    holder.title.text = title
    if (date != null) {
      holder.dateAndTime.text = headerDateFormat.print(date)
      holder.dateAndTime.isGone = false
    } else {
      holder.dateAndTime.isGone = true
    }
  }

  override fun getSectionCount(): Int {
    return items.size
  }

  override fun onBindFooterViewHolder(holder: FeedItemViewHolder, section: Int) { }

  val dateFormatter = DateTimeFormat.forPattern(context.getString(R.string.month_day_format))
  val headerDateFormat = DateTimeFormat.forPattern(context.getString(R.string.header_date_format))
  val timeFormatter = DateTimeFormat.shortTime()

  val selections = PublishSubject.create<Event>()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedItemViewHolder {
    val inflater = LayoutInflater.from(parent.context)
    val layoutRes = when (viewType) {
      VIEW_TYPE_HEADER -> R.layout.feed_header
      else -> R.layout.feed_item
    }
    val view = inflater.inflate(layoutRes, parent, false)
    return FeedItemViewHolder(view)
  }

  private fun cleanupDescription(desc: String): String {
    // Cleanup excess consecutive whitespaces
    return desc.replace("\\s+", " ").replace("\\n", " ")
  }

  private fun getTimeString(start: DateTime, end: DateTime?): String {
    val startDateStr = dateFormatter.print(start)
    val startTimeStr = timeFormatter.print(start)
    val startStr = context.getString(R.string.long_datetime_format, startDateStr, startTimeStr)

    val endStr = end?.let { end ->
      val endTimeStr = timeFormatter.print(end)
      if (start.isSameDayAs(end)) {
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
