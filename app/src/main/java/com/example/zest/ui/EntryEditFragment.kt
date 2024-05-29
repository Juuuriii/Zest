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
import com.example.zest.databinding.FragmentEntryEditBinding


class EntryEditFragment : Fragment() {

    private lateinit var binding: FragmentEntryEditBinding
    private val viewModel: FirebaseViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEntryEditBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()
        setupOnClickListeners()

    }

    private fun setupObservers() {
        observeCurrentEntry()
        observeCurrentEntryTags()
    }

    private fun observeCurrentEntryTags() {
        viewModel.curEntryTags.observe(viewLifecycleOwner) {

            binding.rvTagsEdit.adapter =
                TagEditAdapter(it, requireContext(), viewModel.deleteTag, viewModel.addTag)

        }
    }

    private fun observeCurrentEntry() {
        viewModel.curEntry.observe(viewLifecycleOwner) {

            binding.etTitle.setText(it.title)
            binding.etEntry.setText(it.text)

        }

    }

    private fun setupOnClickListeners() {
        setSaveButtonOnClickListener()
        setBackButtonOnClickListener()
    }

    private fun setSaveButtonOnClickListener() {

        binding.ibSave.setOnClickListener {

            val newTitle = binding.etTitle.text.toString()

            val newText = binding.etEntry.text.toString()

            val newTags = viewModel.curEntryTags.value

            if (binding.etTitle.text.isNotEmpty() && binding.etEntry.text.isNotEmpty()) {

                viewModel.updateEntry(newTitle, newText, newTags ?: mutableListOf())
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