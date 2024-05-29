package com.example.zest.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.zest.FirebaseViewModel
import com.example.zest.MainActivity
import com.example.zest.R
import com.example.zest.data.adapter.JournalEntryAdapter
import com.example.zest.utils.TimeHandler
import com.example.zest.databinding.FragmentJournalBinding
import java.time.LocalDate



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

        setupView()
        setupObservers()
        setupOnClickListeners()


    }

    private fun setupView() {
        viewModel.getEntriesOfDay(viewModel.curDate.value!!)
    }

    private fun setupObservers() {
        observeCurrentDate()
        observeEntriesOfSelectedDay()
    }

    private fun observeEntriesOfSelectedDay() {
        viewModel.entriesOfSelectedDay.observe(viewLifecycleOwner){
            if (it.isNotEmpty()) {
                binding.rvEntries.adapter =
                    JournalEntryAdapter(it,requireContext() ,viewModel.deleteEntry, viewModel.setCurEntry)

                binding.rvEntries.visibility = View.VISIBLE
                binding.tvNoEntries.visibility = View.GONE

            } else {

                binding.rvEntries.visibility = View.GONE
                binding.tvNoEntries.visibility = View.VISIBLE

            }
        }
    }

    private fun observeCurrentDate() {
        viewModel.curDate.observe(viewLifecycleOwner) {

            binding.tvDate.text = TimeHandler().formatDateDayMonthNameYear(it.toString())

        }
    }

    private fun setupOnClickListeners() {
        setNextDateButtonOnClickListener()
        setPreviousDateButtonOnClickListener()
        setDarePickerButtonOnClickListener()
        setOnTvDateOnClickListener()
    }

    private fun setOnTvDateOnClickListener() {
        binding.tvDate.setOnClickListener {

            findNavController().navigate(R.id.calenderFragment)

        }
    }

    private fun setDarePickerButtonOnClickListener() {
        binding.ibDatePicker.setOnClickListener {

            viewModel.datePicker(requireActivity() as MainActivity)

        }
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
        viewModel.setCurDate(LocalDate.now())

    }

}