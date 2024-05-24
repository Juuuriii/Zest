package com.example.zest.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.zest.FirebaseViewModel
import com.example.zest.data.adapter.JournalEntryAdapter
import com.example.zest.data.model.Entry
import com.example.zest.utils.TimeHandler
import com.example.zest.databinding.FragmentJournalBinding


class JournalFragment : Fragment() {

    private lateinit var binding: FragmentJournalBinding
    private val viewModel: FirebaseViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentJournalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()
        setupOnClickListener()



    }

    private fun setupObservers() {
        observeCurrentDate()
        observeJournalDay()
    }

    private fun observeJournalDay() {
        viewModel.journalDay.observe(viewLifecycleOwner){

            if(it != null) {
                binding.rvEntries.adapter = JournalEntryAdapter(it.entries, viewModel.deleteEntry, viewModel.setCurEntry)

                if(it.entries.isEmpty()){
                    binding.tvNoEntries.visibility = View.VISIBLE
                } else {
                    binding.tvNoEntries.visibility = View.INVISIBLE
                }

            }



        }
    }

    private fun observeCurrentDate() {
        viewModel.curDate.observe(viewLifecycleOwner) {

            binding.tvDate.text = TimeHandler().formatLocalDate(it.toString())

            viewModel.getJournalDay(viewModel.curDate.value.toString())


        }
    }

    private fun setupOnClickListener() {
        setNextDateButtonOnClickListener()
        setPreviousDateButtonOnClickListener()
    }

    private fun setPreviousDateButtonOnClickListener() {
        binding.ibPrevDate.setOnClickListener {

            viewModel.curDateMinusOne()

        }
    }

    private fun setNextDateButtonOnClickListener() {
        binding.ibNextDate.setOnClickListener {

            viewModel.curDatePlusOne()

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.resetDateToCurrentDate()

    }

}