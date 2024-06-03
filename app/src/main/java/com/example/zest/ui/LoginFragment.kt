package com.example.zest.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.zest.FirebaseViewModel
import com.example.zest.R

import com.example.zest.databinding.FragmentLoginBinding


class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel: FirebaseViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()
        setupOnClickListeners()
        binding.button.setOnClickListener {

            Log.i("navForgotPassword", "click")

            findNavController().navigate(R.id.forgotPasswordFragment)

        }

    }

    private fun setupObservers() {
        observeCurrentUser()
    }

    private fun observeCurrentUser() {
        viewModel.curUser.observe(viewLifecycleOwner) {
            if (it != null) {
                findNavController().navigate(R.id.homeFragment)
            }
        }
    }

    private fun setupOnClickListeners() {
        setLoginButtonOnClickListener()
        setBackButtonOnClickListener()
        setForgotPasswordOnClickListener()
    }

    private fun setForgotPasswordOnClickListener() {

    }



    private fun setBackButtonOnClickListener() {
        binding.ibBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setLoginButtonOnClickListener() {
        binding.btnLogin.setOnClickListener {

            val email = binding.etEmailLogin.text.toString()
            val password = binding.etPasswordLogin.text.toString()

            if (email.isBlank() || password.isBlank()) {

                Toast.makeText(
                    requireContext(),
                    "Please enter your Login data.",
                    Toast.LENGTH_SHORT
                ).show()

            } else {

                viewModel.login(email, password)
            }
        }
    }


}