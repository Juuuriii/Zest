package com.example.zest.ui

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.zest.FirebaseViewModel
import com.example.zest.R
import com.example.zest.databinding.DialogAddTagBinding
import com.example.zest.databinding.DialogChangeUsernameBinding
import com.example.zest.databinding.DialogConfirmPasswordBinding
import com.example.zest.databinding.DialogVerifyEmailBinding
import com.example.zest.databinding.FragmentSettingsBinding


class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private val viewModel: FirebaseViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()
        setupOnClickListeners()

    }


    private fun changeUsernameDialog() {

        val changeUsernameDialogBinding = DialogChangeUsernameBinding.inflate(layoutInflater)

        val changeUsernameDialog =
            AlertDialog.Builder(requireContext()).setView(changeUsernameDialogBinding.root).show()

        changeUsernameDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        changeUsernameDialogBinding.btnSave.setOnClickListener {

            val newUsername = changeUsernameDialogBinding.etUsername.text.toString()

            if (newUsername != "") {

                viewModel.changeUserName(newUsername)
                changeUsernameDialog.dismiss()

            } else {

                changeUsernameDialog.dismiss()

            }

        }

        changeUsernameDialogBinding.btnCancel.setOnClickListener {

            changeUsernameDialog.dismiss()

        }

    }

    private fun changePasswordDialog() {

        val changePasswordDialogBinding = DialogVerifyEmailBinding.inflate(layoutInflater)

        val changePasswordDialog =
            AlertDialog.Builder(requireContext()).setView(changePasswordDialogBinding.root).show()

        changePasswordDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        changePasswordDialogBinding.tvText.text =
            resources.getString(R.string.reset_password_dialog)

        changePasswordDialogBinding.btnClose.setOnClickListener {

            changePasswordDialog.dismiss()

        }


    }

    private fun changeEmailDialogPassword() {

        val changeEmailDialogBinding = DialogConfirmPasswordBinding.inflate(layoutInflater)

        val changeEmailDialog =
            AlertDialog.Builder(requireContext()).setView(changeEmailDialogBinding.root).show()

        changeEmailDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        changeEmailDialogBinding.btnContinue.setOnClickListener {

            val password = changeEmailDialogBinding.etPassword.text.toString()

            val newEmail = changeEmailDialogBinding.etEmail.text.toString()




            if (password.isNotBlank() && newEmail.isNotBlank()) {

                viewModel.changeMail(password, newEmail, requireContext())

                changeEmailDialog.dismiss()

            } else {

                Toast.makeText(requireContext(),"You need to fill out both fields", Toast.LENGTH_LONG).show()

            }



        }

        changeEmailDialogBinding.btnCancelAddTagDialog.setOnClickListener {

            changeEmailDialog.dismiss()

        }

    }



    private fun setupOnClickListeners() {
        setBackButtonOnClickListener()
        setLogoutButtonOnClickListener()
        setOnUsernameOnClickListener()
        setPasswordChangeOnClickListener()
        setChangeImageOnClickListener()
        setChangeEmailOnClickListener()
    }

    private fun setChangeEmailOnClickListener() {

        binding.tvEmail.setOnClickListener {

            changeEmailDialogPassword()

        }

    }

    private fun setChangeImageOnClickListener() {

        val changeImage =
            registerForActivityResult(
                ActivityResultContracts.StartActivityForResult()
            ) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    result.data?.data?.let { uri ->
                        requireContext().contentResolver.takePersistableUriPermission(
                            uri, Intent.FLAG_GRANT_READ_URI_PERMISSION
                        )

                        binding.ivProfilePic.load(uri)
                        viewModel.uploadProfilePicture(uri)

                    }
                }
            }

        binding.ibAddProfilePic.setOnClickListener {


            val intent =
                Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            changeImage.launch(intent)


        }
    }

    private fun setPasswordChangeOnClickListener() {
        binding.tvPassword.setOnClickListener {

            viewModel.resetPassword()
            changePasswordDialog()

        }
    }

    private fun setOnUsernameOnClickListener() {
        binding.tvUsername.setOnClickListener {
            changeUsernameDialog()
        }
    }

    private fun setLogoutButtonOnClickListener() {
        binding.tvLogout.setOnClickListener {
            viewModel.logout()
            findNavController().navigate(R.id.welcomeFragment)
        }
    }

    private fun setBackButtonOnClickListener() {
        binding.ibBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupObservers() {
        observeProfilePicture()
        observeUserProfile()
        observeCurrentUser()
    }

    private fun observeCurrentUser() {
        viewModel.curUser.observe(viewLifecycleOwner){

            binding.tvEmail.text = it?.email

            if (it == null) {

                findNavController().navigate(R.id.welcomeFragment)

            }

        }
    }

    private fun observeUserProfile() {
        viewModel.curUserProfile.observe(viewLifecycleOwner) {

            binding.tvUsername.text = it.username

        }
    }

    private fun observeProfilePicture() {
        viewModel.profilePic.observe(viewLifecycleOwner) {
            binding.ivProfilePic.load(it)
        }
    }


}

