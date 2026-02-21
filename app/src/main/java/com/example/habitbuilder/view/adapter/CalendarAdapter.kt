package com.example.habitbuilder.view.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.habitbuilder.CalendarItem
import com.example.habitbuilder.view.OnDayClickListener
import com.example.habitbuilder.R
import com.example.habitbuilder.Status
import java.util.Calendar

class CalendarAdapter(
    private val items: List<CalendarItem>,
    private val listener: OnDayClickListener
) : RecyclerView.Adapter<CalendarAdapter.DayVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayVH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.day_cell, parent, false)
        return DayVH(view)
    }

    override fun onBindViewHolder(holder: DayVH, position: Int) {
        holder.bind(items[position], listener)
    }

    override fun getItemCount() = items.size

    class DayVH(view: View) : RecyclerView.ViewHolder(view) {
        private val dayText = view.findViewById<TextView>(R.id.dayText)

        fun bind(item: CalendarItem, listener: OnDayClickListener) {
            when (item) {
                is CalendarItem.Empty -> {
                    bindEmpty()
                }

                is CalendarItem.Day -> {
                    bindDay(item, listener)
                }
            }
        }

        private fun bindDay(
            item: CalendarItem.Day,
            listener: OnDayClickListener
        ) {
            dayText.text = item.date.get(Calendar.DAY_OF_MONTH).toString()
            dayText.setBackgroundColor(
                when (item.status) {
                    Status.GREEN -> Color.GREEN
                    Status.YELLOW -> Color.YELLOW
                    Status.RED -> Color.RED
                    Status.NONE -> Color.GRAY
                }
            )
            dayText.setOnClickListener {
                listener.onDayClick(item.date)
            }
        }

        private fun bindEmpty() {
            dayText.text = ""
            dayText.setBackgroundColor(Color.TRANSPARENT)
        }
    }
}