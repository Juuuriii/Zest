package com.example.zest.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.zest.FirebaseViewModel
import com.example.zest.data.TimeHandler
import com.example.zest.databinding.FragmentJournalBinding


class JournalFragment : Fragment() {

    private lateinit var binding: FragmentJournalBinding
    private val viewModel: FirebaseViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentJournalBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.curDate.observe(viewLifecycleOwner){

            binding.tvDate.text = TimeHandler().formatLocalDate(it.toString())

        }

        binding.ibPrevDate.setOnClickListener {

            viewModel.curDateMinusOne()

        }

        binding.ibNextDate.setOnClickListener {

            viewModel.curDatePlusOne()

        }

    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.resetDateToCurrentDate()

    }

}