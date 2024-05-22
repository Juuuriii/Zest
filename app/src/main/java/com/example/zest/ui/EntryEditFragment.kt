package com.example.zest.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.zest.FirebaseViewModel
import com.example.zest.R
import com.example.zest.data.adapter.TagEditAdapter
import com.example.zest.data.model.Entry
import com.example.zest.databinding.FragmentEntryEditBinding


class EntryEditFragment : Fragment() {

    private lateinit var binding: FragmentEntryEditBinding
    private val viewModel: FirebaseViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEntryEditBinding.inflate(inflater,container,false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()
        setupOnClickListener()

    }

    private fun setupObservers() {
        observeCurrentEntry()
    }

    private fun observeCurrentEntry() {
        viewModel.curEntry.observe(viewLifecycleOwner){

            binding.etTitle.setText(it.title)
            binding.etEntry.setText(it.text)

            binding.rvTagsEdit.adapter = TagEditAdapter(it.tags.toList(), requireContext(), viewModel.deleteTagTest, viewModel.addTag)

        }
    }

    private fun setupOnClickListener(){
        setSaveButtonOnClickListener()
        setBackButtonOnClickListener()
    }

    private fun setSaveButtonOnClickListener() {

        binding.ibSave.setOnClickListener {

            if(binding.etTitle.text.isNotEmpty() && binding.etEntry.text.isNotEmpty()) {

                viewModel.updateEntry(
                    Entry(
                        title = binding.etTitle.text.toString(),
                        text = binding.etEntry.text.toString(),
                        tags = viewModel.curEntry.value!!.tags,
                        time = viewModel.curEntry.value!!.time,
                        date = viewModel.curEntry.value!!.date
                    )
                )
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