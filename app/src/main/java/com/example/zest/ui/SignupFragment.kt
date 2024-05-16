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
import com.example.zest.databinding.FragmentSignupBinding


class SignupFragment : Fragment() {


    private lateinit var binding: FragmentSignupBinding

    private  val viewModel: FirebaseViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignupBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




        binding.btnSignup.setOnClickListener {

            val username = binding.etUsernameSignup.text.toString()
            val email = binding.etEmailSignup.text.toString()
            val password = binding.etPasswordSignup.text.toString()

            viewModel.register(email, password, username) {

                findNavController().navigate(R.id.loginFragment)

            }

        }




        binding.ibBack.setOnClickListener {

            findNavController().navigateUp()

        }

    }



}