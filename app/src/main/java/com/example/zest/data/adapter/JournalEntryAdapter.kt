package com.example.zest.data.adapter

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.zest.R
import com.example.zest.data.model.Entry
import com.example.zest.databinding.DialogDeleteEntryBinding
import com.example.zest.databinding.ItemJournalBinding
import com.example.zest.utils.TimeHandler

class JournalEntryAdapter(

    private val dataset: List<Entry>,
    private val context: Context,
    private val deleteEntry: (String) -> Unit,
    private val setCurEntry: (Entry) -> Unit


) : RecyclerView.Adapter<JournalEntryAdapter.ItemViewHolder>() {

    inner class ItemViewHolder(val binding: ItemJournalBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemJournalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val entry = dataset[position]

        holder.binding.tvTime.text = TimeHandler().formatDateTimeHoursMins(entry.time)
        holder.binding.tvEntryText.text = entry.text
        holder.binding.tvTitle.text = entry.title
        holder.binding.rvTags.adapter = TagAdapter(entry.tags)


        holder.binding.ibEdit.setOnClickListener {

            setCurEntry(entry)
            holder.itemView.findNavController().navigate(R.id.entryEditFragment2)

        }

        holder.binding.ibDelete.setOnClickListener {

            deleteEntryDialog(entry.time)

        }
    }

    private fun deleteEntryDialog(time: String) {

        val deleteEntryDialogBinding =
            DialogDeleteEntryBinding.inflate(LayoutInflater.from(context))

        val deleteEntryDialog =
            AlertDialog.Builder(context).setView(deleteEntryDialogBinding.root).show()


        deleteEntryDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        deleteEntryDialogBinding.btnDeleteDeleteEntryDialog.setOnClickListener {


            deleteEntry(time)
            deleteEntryDialog.dismiss()

        }

        deleteEntryDialogBinding.btnCancelDeleteEntryDialog.setOnClickListener {

            deleteEntryDialog.dismiss()

        }
    }

}