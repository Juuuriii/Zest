package com.example.zest.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.zest.FirebaseViewModel
import com.example.zest.MainActivity
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
    }

    private fun observeCurrentDate() {
        viewModel.curDate.observe(viewLifecycleOwner) {

            binding.tvDate.text = TimeHandler().formatDateDayMonthNameYear(it.toString())

            viewModel.getEntryRef(it.toString()).get().addOnSuccessListener { querySnapshot ->

                val entryList = querySnapshot.map { it.toObject(Entry::class.java) }

                if (entryList.isNotEmpty()) {
                    binding.rvEntries.adapter =
                        JournalEntryAdapter(entryList,requireContext() ,viewModel.deleteEntry, viewModel.setCurEntry)

                    binding.rvEntries.visibility = View.VISIBLE
                    binding.tvNoEntries.visibility = View.GONE

                } else {

                    binding.rvEntries.visibility = View.GONE
                    binding.tvNoEntries.visibility = View.VISIBLE

                }

                Log.i("ΩgetEntries", " Success Result => $entryList")

            }.addOnFailureListener {

                Log.i("ΩgetEntries", "Fail Result => ${it.message}")

            }
        }
    }

    private fun setupOnClickListener() {
        setNextDateButtonOnClickListener()
        setPreviousDateButtonOnClickListener()
        setDarePickerButtonOnClickListener()
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
        viewModel.resetDateToCurrentDate()

    }

}