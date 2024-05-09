package com.example.zest.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.SnapHelper
import com.example.zest.FirebaseViewModel

import com.example.zest.R
import com.example.zest.data.adapter.QuoteAdapter
import com.example.zest.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {


    private lateinit var binding: FragmentHomeBinding

    private val viewModel: FirebaseViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.username.observe(viewLifecycleOwner){

            binding.tvUsername.text = it

        }


        binding.btnLogout.setOnClickListener {

            viewModel.logout()

        }

        binding.cvProfile.setOnClickListener {

            viewModel.getUsername()

        }

        viewModel.curUser.observe(viewLifecycleOwner){

            if (it == null){
                findNavController().navigate(R.id.welcomeFragment)
            }

        }

        viewModel.quotes.observe(viewLifecycleOwner){

            val helper: SnapHelper = PagerSnapHelper()
            helper.attachToRecyclerView(binding.rvQuote)

            binding.rvQuote.adapter = QuoteAdapter(it)

        }
    }
}