package com.example.zest.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.zest.FirebaseViewModel
import com.example.zest.data.adapter.CalendarAdapter
import com.example.zest.databinding.FragmentCalenderBinding
import com.example.zest.utils.TimeHandler


class CalenderFragment : Fragment() {


    private lateinit var binding: FragmentCalenderBinding
    private val viewModel: FirebaseViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCalenderBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        setupObservers()
        setupOnClickListeners()
    }

    private fun setupView() {
        viewModel.setCurrentCalendarMonth()
        viewModel.getEntriesOfMonth(viewModel.curCalendarMonth.value!!)

    }

    private fun setupOnClickListeners() {
        setNextMonthButtonListener()
        setPreviousMonthButtonListener()
    }

    private fun setPreviousMonthButtonListener() {
        binding.ibMonthBack.setOnClickListener {

            viewModel.previousCurrentMonth()

        }
    }

    private fun setNextMonthButtonListener() {
        binding.ibMonthForward.setOnClickListener {

            viewModel.nextCurrentMonth()

        }
    }

    private fun setupObservers() {
        observeCurrentCalendarMonthDays()
        observeCurrentCalendarMonth()
    }

    private fun observeCurrentCalendarMonthDays() {
        viewModel.curCalenderMonthDays.observe(viewLifecycleOwner){

            binding.rvCalendar.adapter = CalendarAdapter(it, viewModel.setCurDate, viewModel.curCalendarMonth)

        }
    }

    private fun observeCurrentCalendarMonth() {


        viewModel.curCalendarMonth.observe(viewLifecycleOwner){

            binding.tvDateCalendar.text = TimeHandler().formatYearMonth(it)

        }


    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.setCurrentCalendarMonth()
    }


}