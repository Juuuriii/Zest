package com.example.zest.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.zest.R
import com.example.zest.data.model.CalendarDay
import com.example.zest.databinding.ItemCalendarTodayBinding
import com.example.zest.databinding.ItemCalendarBinding
import com.example.zest.databinding.ItemCalendarEntryBinding
import com.example.zest.databinding.ItemCalendarTodayEntryBinding
import java.time.LocalDate
import java.time.YearMonth


class CalendarAdapter(
    private val dataset: List<CalendarDay>,
    val setCurDate: (localDate: LocalDate) -> Unit,
    private val curCalendarMonth: LiveData<YearMonth>
) : RecyclerView.Adapter<ViewHolder>() {

    private val withoutEntryView = 1

    private val withEntryView = 2

    private val isTodayView = 3

    private val isTodayEntryView = 4

    inner class WithoutEntryViewHolder(val binding: ItemCalendarBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class WithEntryViewHolder(val binding: ItemCalendarEntryBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class IsTodayViewHolder(val binding: ItemCalendarTodayBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class IsTodayEntryViewHolder(val binding: ItemCalendarTodayEntryBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun getItemViewType(position: Int): Int {

        val day = dataset[position]


        if (day.isToday && day.hasEntry) {

            return isTodayEntryView

        }

        if (day.isToday) {

            return isTodayView

        }

        return if (day.hasEntry) {
            withEntryView
        } else {
            withoutEntryView
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return when (viewType) {

            withEntryView -> {
                val binding =
                    ItemCalendarEntryBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                WithEntryViewHolder(binding)
            }

            withoutEntryView -> {
                val binding =
                    ItemCalendarBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                WithoutEntryViewHolder(binding)
            }


            isTodayEntryView -> {

                val binding = ItemCalendarTodayEntryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return IsTodayEntryViewHolder(binding)

            }

            else -> {
                val binding =
                    ItemCalendarTodayBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                IsTodayViewHolder(binding)
            }
        }
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        val day = dataset[position]





        when (holder) {

            is WithoutEntryViewHolder -> {

                holder.binding.tvDay.text = day.day

                holder.binding.tvDay.setOnClickListener {

                    val date: LocalDate = if (day.day != "") {
                        curCalendarMonth.value!!.atDay(day.day.toInt())
                    } else {
                        curCalendarMonth.value!!.atDay(1)
                    }


                    if (LocalDate.now() >= date) {
                        setCurDate(date)
                        holder.itemView.findNavController().navigate(R.id.journalFragment)
                    }
                }

            }

            is WithEntryViewHolder -> {
                holder.binding.tvDay.text = day.day

                holder.binding.tvDay.setOnClickListener {

                    val date: LocalDate = if (day.day != "") {
                        curCalendarMonth.value!!.atDay(day.day.toInt())
                    } else {
                        curCalendarMonth.value!!.atDay(1)
                    }

                    if (LocalDate.now() >= date) {
                        setCurDate(date)
                        holder.itemView.findNavController().navigate(R.id.journalFragment)
                    }
                }
            }

            is IsTodayViewHolder -> {
                holder.binding.tvDay.text = day.day

                holder.binding.tvDay.setOnClickListener {

                    val date: LocalDate = if (day.day != "") {
                        curCalendarMonth.value!!.atDay(day.day.toInt())
                    } else {
                        curCalendarMonth.value!!.atDay(1)
                    }

                    if (LocalDate.now() >= date) {
                        setCurDate(date)
                        holder.itemView.findNavController().navigate(R.id.journalFragment)

                    }
                }
            }

            is IsTodayEntryViewHolder -> {
                holder.binding.tvDay.text = day.day

                holder.binding.tvDay.setOnClickListener {

                    val date: LocalDate = if (day.day != "") {
                        curCalendarMonth.value!!.atDay(day.day.toInt())
                    } else {
                        curCalendarMonth.value!!.atDay(1)
                    }

                    if (LocalDate.now() >= date) {
                        setCurDate(date)
                        holder.itemView.findNavController().navigate(R.id.journalFragment)

                    }
                }

            }

        }

    }


}