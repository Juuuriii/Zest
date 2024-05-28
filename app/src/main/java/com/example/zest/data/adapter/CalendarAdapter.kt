package com.example.zest.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.zest.R
import com.example.zest.data.model.CalendarDay
import com.example.zest.databinding.CalendarItemBinding
import com.example.zest.databinding.CalendarItemEntryBinding
import com.example.zest.databinding.CalenderItemTodayBinding
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

class CalendarAdapter(
    private val dataset: List<CalendarDay>,
    val setCurDate: (localDate: LocalDate) -> Unit,
    val curCalendarMonth: LiveData<YearMonth>
) : RecyclerView.Adapter<ViewHolder>() {

    private val withoutEntryView = 1

    private val withEntryView = 2

    private val isTodayView = 3

    inner class WithoutEntryViewHolder(val binding: CalendarItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class WithEntryViewHolder(val binding: CalendarItemEntryBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class IsTodayViewHolder(val binding: CalenderItemTodayBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun getItemViewType(position: Int): Int {

        val day = dataset[position]

        if (day.isToday) {

            isTodayView

        }

        return if (day.hasEntry) {
            withEntryView
        } else {
            withoutEntryView
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (viewType == withoutEntryView) {
            val binding =
                CalendarItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            WithoutEntryViewHolder(binding)

        } else if (viewType == withEntryView) {

            val binding =
                CalendarItemEntryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            WithEntryViewHolder(binding)
        } else {
            val binding =
                CalenderItemTodayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            IsTodayViewHolder(binding)

        }


    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        val day = dataset[position]

        val date: LocalDate = if (day.day != "") {
            curCalendarMonth.value!!.atDay(day.day.toInt())
        } else {
            curCalendarMonth.value!!.atDay(1)
        }


        if (holder is WithoutEntryViewHolder) {

            holder.binding.tvDay.text = day.day

            holder.binding.tvDay.setOnClickListener {


                if (LocalDate.now() >= date) {
                    setCurDate(date)
                    holder.itemView.findNavController().navigate(R.id.journalFragment)

                }
            }

        } else if (holder is WithEntryViewHolder) {

            holder.binding.tvDay.text = day.day

            holder.binding.tvDay.setOnClickListener {

                if (LocalDate.now() >= date) {
                    setCurDate(date)
                    holder.itemView.findNavController().navigate(R.id.journalFragment)

                }
            }
        } else if (holder is IsTodayViewHolder) {
            holder.binding.tvDay.text = day.day

            holder.binding.tvDay.setOnClickListener {

                if (LocalDate.now() >= date) {
                    setCurDate(date)
                    holder.itemView.findNavController().navigate(R.id.journalFragment)

                }
            }


        }
    }

    override fun getItemCount(): Int {
        return dataset.size
    }


}