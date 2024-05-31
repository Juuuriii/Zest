package com.example.zest.ui

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.zest.FirebaseViewModel
import com.example.zest.R
import com.example.zest.databinding.DialogVerifyEmailBinding
import com.example.zest.databinding.FragmentSignupBinding


class SignupFragment : Fragment() {


    private lateinit var binding: FragmentSignupBinding

    private val viewModel: FirebaseViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupOnClickListeners()

    }

    private fun setupOnClickListeners() {
        setSignUpButtonOnClickListener()
        setBackButtonOnClickListener()
    }

    private fun setBackButtonOnClickListener() {
        binding.ibBack.setOnClickListener {

            findNavController().navigateUp()

        }
    }

    fun verifyEmailDialog() {

        val verifyEmailDialogBinding = DialogVerifyEmailBinding.inflate(layoutInflater)

        val verifyEmailDialog = AlertDialog.Builder(requireContext()).setView(verifyEmailDialogBinding.root).show()

        verifyEmailDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        verifyEmailDialogBinding.btnClose.setOnClickListener {

            verifyEmailDialog.dismiss()

        }

    }
    private fun setSignUpButtonOnClickListener() {
        binding.btnSignup.setOnClickListener {

            val username = binding.etUsernameSignup.text.toString()
            val email = binding.etEmailSignup.text.toString()
            val password = binding.etPasswordSignup.text.toString()



            viewModel.register(email, password, username) {

                verifyEmailDialog()

                findNavController().navigate(R.id.loginFragment)

            }

        }
    }


}