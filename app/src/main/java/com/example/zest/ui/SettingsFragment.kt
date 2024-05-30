package com.example.zest.ui

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.zest.FirebaseViewModel
import com.example.zest.MainActivity
import com.example.zest.R
import com.example.zest.databinding.DialogDeleteEntryBinding
import com.example.zest.databinding.DialogChangeUsernameBinding
import com.example.zest.databinding.FragmentSettingsBinding
import java.time.LocalDate

//TODO(clean up code)

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

        binding.tvPassword.setOnClickListener {

            viewModel.resetPassword()

        }

        setupObservers()
        setupOnClickListeners()



        val changeImage =
            registerForActivityResult(
                ActivityResultContracts.StartActivityForResult()
            ) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    result.data?.data?.let { uri ->
                        requireContext().contentResolver.takePersistableUriPermission(
                            uri, Intent.FLAG_GRANT_READ_URI_PERMISSION
                        )

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



    private fun changeUsernameDialog() {

        val changeUsernameDialogBinding = DialogChangeUsernameBinding.inflate(layoutInflater)

        val changeUsernameDialog = AlertDialog.Builder(requireContext()).setView(changeUsernameDialogBinding.root).show()

        changeUsernameDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        changeUsernameDialogBinding.btnSave.setOnClickListener {

            val newUsername = changeUsernameDialogBinding.etUsername.text.toString()

            if(newUsername != "") {

                viewModel.changeUserName(newUsername)
                changeUsernameDialog.dismiss()

            } else {

                changeUsernameDialog.dismiss()

            }

        }

    }


    private fun setupOnClickListeners() {
        setBackButtonOnClickListener()
        setLogoutButtonOnClickListener()
        setOnUsernameOnClickListener()
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
    }

    private fun observeUserProfile() {
        viewModel.curUserProfile.observe(viewLifecycleOwner) {

            binding.tvUsername.text = it.username

            binding.tvEmail.text = it.userEmail

        }
    }

    private fun observeProfilePicture() {
        viewModel.profilePic.observe(viewLifecycleOwner) {
            binding.ivProfilePic.load(it)
        }
    }


}

