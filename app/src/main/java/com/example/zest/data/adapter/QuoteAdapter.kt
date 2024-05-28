package com.example.zest.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Adapter
import androidx.recyclerview.widget.RecyclerView
import com.example.zest.data.model.Quote
import com.example.zest.databinding.QuoteItemBinding

class QuoteAdapter(

    private val quoteList: List<Quote>

): RecyclerView.Adapter<QuoteAdapter.ItemViewHolder>() {

    inner class ItemViewHolder(val binding: QuoteItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = QuoteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return quoteList.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        val quote = quoteList[position]

        val author = " - " + quote.author

        val quoteText = "\"" + quote.text + "\""

        holder.binding.tvQuote.text = quoteText
        holder.binding.tvAuthor.text = author

    }

}