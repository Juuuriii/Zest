package com.example.zest.data.adapter

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.zest.databinding.DialogAddTagBinding
import com.example.zest.databinding.DialogDeleteEntryBinding
import com.example.zest.databinding.ItemEditAddbuttonItemBinding
import com.example.zest.databinding.ItemEditTagBinding

class TagEditAdapter(
    private val tagList: List<String>,
    private val context: Context,
    private val deleteTag: (Int) -> Unit,
    private val addTag: (String) -> Unit
) : RecyclerView.Adapter<ViewHolder>() {

    private val tagView = 1

    private val addView = 2

    inner class TagViewHolder(val binding: ItemEditTagBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class AddViewHolder(val binding: ItemEditAddbuttonItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val dataset: MutableList<String> = tagList.toMutableList()

    init {
        dataset.add("button")
    }

    override fun getItemViewType(position: Int): Int {
        val item = dataset[position]

        return if (position == dataset.size - 1) {
            addView
        } else {
            tagView
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (viewType == tagView) {
            val binding =
                ItemEditTagBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            TagViewHolder(binding)
        } else {
            val binding = ItemEditAddbuttonItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            AddViewHolder(binding)
        }
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tag = dataset[position]

        if (holder is TagViewHolder) {

            holder.binding.tvTag.text = tag

            holder.binding.ibDeleteTag.setOnClickListener {

                deleteTag(position)
            }
        } else if (holder is AddViewHolder) {

            holder.binding.btnAddTag.setOnClickListener {
                addTagDialog()
            }
        }
    }

    private fun addTagDialog(){

        val addTagDialogBinding = DialogAddTagBinding.inflate(LayoutInflater.from(context))

        val addTagDialog = AlertDialog.Builder(context).setView(addTagDialogBinding.root).show()

        addTagDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        addTagDialogBinding.btnAddTag.setOnClickListener {

            val tag = addTagDialogBinding.etTag.text.toString()

            addTag(tag)

            addTagDialog.dismiss()
        }
        addTagDialogBinding.btnCancelAddTagDialog.setOnClickListener {

            addTagDialog.dismiss()

        }


    }

}