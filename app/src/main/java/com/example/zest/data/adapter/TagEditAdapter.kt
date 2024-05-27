package com.example.zest.data.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.zest.databinding.TagEditAddbuttonItemBinding
import com.example.zest.databinding.TagEditItemBinding

class TagEditAdapter(
    private val tagList: List<String>,
    private val context: Context,
    private val deleteTag: (Int) -> Unit,
    private val addTag: (Context) -> Unit
) : RecyclerView.Adapter<ViewHolder>() {

    private val tagView = 1

    private val addView = 2

    inner class TagViewHolder(val binding: TagEditItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class AddViewHolder(val binding: TagEditAddbuttonItemBinding) :
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
        if (viewType == tagView) {
            val binding =
                TagEditItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return TagViewHolder(binding)
        } else {
            val binding = TagEditAddbuttonItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return AddViewHolder(binding)
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
                addTag(context)
            }
        }
    }


}