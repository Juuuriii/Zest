package com.example.zest.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.zest.FirebaseViewModel
import com.example.zest.R
import com.example.zest.databinding.FragmentWelcomeBinding


class WelcomeFragment: Fragment() {


    private lateinit var binding: FragmentWelcomeBinding
    private  val viewModel: FirebaseViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWelcomeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.curUser.observe(viewLifecycleOwner){
            if(it != null) {
                findNavController().navigate(R.id.homeFragment)
            }

        }

        binding.tvLogin.setOnClickListener {

            findNavController().navigate(R.id.loginFragment)

        }

        binding.btnSignup.setOnClickListener {

            findNavController().navigate(R.id.signupFragment)

        }
    }


}