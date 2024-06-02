package com.example.zest.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.zest.FirebaseViewModel
import com.example.zest.data.adapter.SearchAdapter
import com.example.zest.databinding.FragmentSearchBinding


class SearchFragment : Fragment() {


    private lateinit var binding: FragmentSearchBinding
    private val viewModel: FirebaseViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setBackgroundAlpha()
        setupObservers()
        binding.ibSearch.setOnClickListener {

            val searchTerm = binding.actvSearch.text.toString()

            viewModel.searchEntries(searchTerm)

        }

    }

    private fun setupObservers() {
        observeSearchResults()
    }

    private fun observeSearchResults() {
        viewModel.searchResults.observe(viewLifecycleOwner) {

            when (it.size) {

                0 -> {
                    binding.tvSearchResultsCount.visibility = View.GONE
                }

                1 -> {
                    binding.tvSearchResultsCount.visibility = View.VISIBLE
                    binding.tvSearchResultsCount.text = "${it.size} Search Result Found!"
                }

                else -> {
                    binding.tvSearchResultsCount.visibility = View.VISIBLE
                    binding.tvSearchResultsCount.text = "${it.size} Search Results Found!"
                }
            }



            binding.rvSearch.adapter = SearchAdapter(it)

        }
    }

    private fun setBackgroundAlpha() {
        binding.clSearchScreen.background.alpha = (255 * 0.6).toInt()
    }


}