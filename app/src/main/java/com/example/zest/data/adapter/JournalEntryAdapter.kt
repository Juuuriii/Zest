package com.example.zest.data.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.zest.R
import com.example.zest.data.model.Entry
import com.example.zest.databinding.JournalItemBinding

class JournalEntryAdapter(

    private val dataset: List<Entry>,
    private val deleteEntry: (String) -> Unit,
    private val setCurEntry: (Entry) -> Unit


):RecyclerView.Adapter<JournalEntryAdapter.ItemViewHolder>() {

    inner class ItemViewHolder(val binding: JournalItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = JournalItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val entry = dataset[position]

        holder.binding.tvEntryText.text = entry.text
        holder.binding.tvTitle.text = entry.title
        holder.binding.rvTags.adapter = TagAdapter(entry.tags)

        holder.binding.cvJournalEntry.setOnClickListener {

            setCurEntry(entry)
            Log.i("ΩcurrentEntry", "${entry.title}")
            holder.itemView.findNavController().navigate(R.id.entryEditFragment2)


        }

        holder.binding.ibDelete.setOnClickListener {

            deleteEntry(entry.time)

        }

    }

}