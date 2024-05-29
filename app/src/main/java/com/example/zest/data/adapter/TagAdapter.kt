package com.example.zest.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.zest.databinding.ItemTagBinding

class TagAdapter(
    private val dataset: List<String>
):RecyclerView.Adapter<TagAdapter.ItemViewHolder>() {

    inner class ItemViewHolder(val binding: ItemTagBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemTagBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val tag = dataset[position]

        holder.binding.tvTag.text = tag

    }

}