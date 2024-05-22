package com.example.zest.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.zest.FirebaseViewModel
import com.example.zest.R
import com.example.zest.data.adapter.TagEditAdapter
import com.example.zest.databinding.FragmentEntryAddBinding


class EntryAddFragment : Fragment() {


    private lateinit var binding: FragmentEntryAddBinding

    private val viewModel: FirebaseViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEntryAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setupView()
        setupObservers()
        setupOnClickListener()


    }

    private fun setupView() {
        viewModel.setEmptyEntry()
    }

    private fun setupObservers() {
        observeCurrentEntry()
    }

    private fun observeCurrentEntry() {
        viewModel.curEntry.observe(viewLifecycleOwner) { entry ->

            binding.rvTagsEdit.adapter =
                TagEditAdapter(entry.tags, requireContext() , viewModel.deleteTag, viewModel.addTag)

        }
    }

    private fun setupOnClickListener() {
        setSaveButtonOnClickListener()
        setBackButtonOnClickListener()
    }

    private fun setSaveButtonOnClickListener() {
        binding.ibSave.setOnClickListener {

            val title = binding.etTitle.text.toString()

            val text = binding.etEntry.text.toString()

            val tags = viewModel.curEntry.value!!.tags

            if (title.isNotEmpty() && text.isNotEmpty()) {

                viewModel.createEntry(title, text, tags)
                findNavController().navigate(R.id.journalFragment)

            }
        }

    }

    private fun setBackButtonOnClickListener() {

        binding.ibBack.setOnClickListener {

            findNavController().navigateUp()

        }

    }

}