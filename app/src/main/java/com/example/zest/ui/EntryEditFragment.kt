package com.example.zest.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.zest.FirebaseViewModel
import com.example.zest.databinding.FragmentEntryEditBinding


class EntryEditFragment : Fragment() {


    private lateinit var binding: FragmentEntryEditBinding

    private val viewModel: FirebaseViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEntryEditBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupOnClickListener()





    }

    private fun setupOnClickListener(){
        saveJournalEntry()
        navigateBack()
    }

    private fun saveJournalEntry() {

        binding.ibSave.setOnClickListener {

            val title = binding.etTitle.text.toString()

            val text = binding.etEntry.text.toString()

            viewModel.createEntry(title, text)

        }

    }

    private fun navigateBack() {

        binding.ibBack.setOnClickListener {

            findNavController().navigateUp()

        }

    }

}