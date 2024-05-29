package com.example.zest.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.zest.FirebaseViewModel
import com.example.zest.MainActivity
import com.example.zest.R
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

        setupObservers()
        setupOnClickListeners()


        var imageUri: Uri

        val changeImage =
            registerForActivityResult(
                ActivityResultContracts.StartActivityForResult()
            ) {
                if (it.resultCode == Activity.RESULT_OK) {
                    it.data?.data?.let { it1 ->
                        requireContext().contentResolver.takePersistableUriPermission(
                            it1, Intent.FLAG_GRANT_READ_URI_PERMISSION
                        )
                        imageUri = it1

                        viewModel.uploadProfilePicture(it1)

                    }
                }
            }

        binding.ibAddProfilePic.setOnClickListener {



            val intent =
                Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            changeImage.launch(intent)


        }



    }

    private fun setupOnClickListeners() {
       setBackButtonOnClickListener()
        setLogoutButtonOnClickListener()
    }

    private fun setLogoutButtonOnClickListener() {
        binding.tvLogout.setOnClickListener {
            viewModel.logout()
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
        viewModel.curUserProfile.observe(viewLifecycleOwner){

            binding.tvUsername.text = it.username

            binding.tvEmail.text = it.userEmail

        }
    }

    private fun observeProfilePicture() {
        viewModel.profilePic.observe(viewLifecycleOwner){
            binding.ivProfilePic.load(it)
        }
    }


}

