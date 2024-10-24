package com.example.zest.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.zest.R
import com.example.zest.data.model.Entry
import com.example.zest.databinding.ItemSearchBinding
import com.example.zest.utils.TimeHandler
import java.text.SimpleDateFormat
import java.time.LocalDate

class SearchAdapter(
    private val dataset: List<Entry>,
    private val setCurDate:(LocalDate) -> Unit,
    private val searchEntries:(String) -> Unit
): RecyclerView.Adapter<SearchAdapter.ItemViewHolder>() {

    inner class ItemViewHolder(val binding: ItemSearchBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val entry = dataset[position]

        holder.binding.tvTitleSearch.text = entry.title
        holder.binding.tvDateSearch.text = TimeHandler().formatLocalDate(entry.date)
        holder.binding.tvTimeSearch.text = TimeHandler().formatDateTimeHoursMins(entry.time)
        holder.binding.tvTextSearch.text = entry.text
        holder.binding.rvTags.adapter = TagAdapter(entry.tags, searchEntries)

        holder.binding.cvSearchItem.setOnClickListener {

            val localDate = LocalDate.of(entry.year.toInt(), entry.month.toInt(), entry.day.toInt())

            setCurDate(localDate)

            holder.itemView.findNavController().navigate(R.id.journalFragment)

        }

    }
}