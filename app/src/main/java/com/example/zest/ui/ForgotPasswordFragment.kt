package com.example.zest.ui

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.zest.FirebaseViewModel
import com.example.zest.R
import com.example.zest.databinding.DialogVerifyEmailBinding
import com.example.zest.databinding.FragmentForgotPasswordBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth


class ForgotPasswordFragment : Fragment() {

    lateinit var binding: FragmentForgotPasswordBinding

    private val viewModel: FirebaseViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentForgotPasswordBinding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

      setupOnClickListeners()

    }

    private fun setupOnClickListeners() {
        setChangePasswordButtonClickListener()
        setOnBackButtonClickListener()
    }

    private fun setOnBackButtonClickListener() {
        binding.ibBack.setOnClickListener {

            findNavController().navigateUp()

        }
    }

    private fun setChangePasswordButtonClickListener() {
        binding.btnChangePassword.setOnClickListener {

            val email = binding.etChangePassword.text.toString()

            if (email.isNotBlank()){

                Firebase.auth.sendPasswordResetEmail(email)
                changePasswordDialog(email)

            } else {

                Toast.makeText(requireContext(),"Please enter a valid Email.", Toast.LENGTH_SHORT).show()

            }
        }
    }

    private fun changePasswordDialog(email: String) {

        val changePasswordDialogBinding = DialogVerifyEmailBinding.inflate(layoutInflater)

        val changePasswordDialog = AlertDialog.Builder(requireContext()).setView(changePasswordDialogBinding.root).show()

        changePasswordDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        changePasswordDialogBinding.tvText.text = "We send an Email to change your Password to $email."

        changePasswordDialog.setOnDismissListener {

            findNavController().navigateUp()

        }

        changePasswordDialogBinding.btnClose.setOnClickListener {

            changePasswordDialog.dismiss()

        }

    }

}