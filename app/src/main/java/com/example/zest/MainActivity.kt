package com.example.zest

import android.os.Bundle
import android.provider.CalendarContract.Colors
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.zest.databinding.ActivityMainBinding
import com.google.rpc.context.AttributeContext.Resource
import com.google.type.Color
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent

//TODO(Loading Screen implement Logo with Text)


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {

        installSplashScreen()

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.materialToolbar
        setSupportActionBar(toolbar)

        val navHost =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        NavigationUI.setupWithNavController(binding.bottomNav, navHost.navController, false)


        navHost.navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment -> {
                    binding.bottomNav.visibility = View.VISIBLE
                    binding.floatingActionButton.visibility = View.VISIBLE
                    binding.materialToolbar.visibility = View.VISIBLE
                    binding.materialToolbar.setBackgroundColor(resources.getColor(R.color.primary_Alabaster))
                    this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
                }


                R.id.searchFragment -> {
                    binding.bottomNav.visibility = View.VISIBLE
                    binding.floatingActionButton.visibility = View.VISIBLE
                    binding.materialToolbar.visibility = View.VISIBLE
                    binding.materialToolbar.setBackgroundColor(resources.getColor(R.color.primary_Alabaster))
                    this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)

                }

                R.id.journalFragment -> {
                    binding.bottomNav.visibility = View.VISIBLE
                    binding.floatingActionButton.visibility = View.VISIBLE
                    binding.materialToolbar.visibility = View.VISIBLE
                    binding.materialToolbar.setBackgroundColor(resources.getColor(R.color.primary_coral))
                    this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
                }

                R.id.calenderFragment -> {
                    binding.bottomNav.visibility = View.VISIBLE
                    binding.floatingActionButton.visibility = View.VISIBLE
                    binding.materialToolbar.visibility = View.VISIBLE
                    binding.materialToolbar.setBackgroundColor(resources.getColor(R.color.primary_Alabaster))
                    this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
                }

                R.id.signupFragment -> {

                    binding.bottomNav.visibility = View.GONE
                    binding.floatingActionButton.visibility = View.GONE
                    binding.materialToolbar.visibility = View.GONE
                    binding.materialToolbar.setBackgroundColor(resources.getColor(R.color.primary_Alabaster))
                    this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)


                }

                R.id.loginFragment -> {

                    binding.bottomNav.visibility = View.GONE
                    binding.floatingActionButton.visibility = View.GONE
                    binding.materialToolbar.visibility = View.GONE
                    binding.materialToolbar.setBackgroundColor(resources.getColor(R.color.primary_Alabaster))
                    this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)


                }

                else -> {
                    binding.bottomNav.visibility = View.GONE
                    binding.floatingActionButton.visibility = View.GONE
                    binding.materialToolbar.visibility = View.GONE
                    binding.materialToolbar.setBackgroundColor(resources.getColor(R.color.primary_Alabaster))
                    this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
                }

            }
        }

        binding.floatingActionButton.setOnClickListener {

            binding.fragmentContainerView.findNavController().navigate(R.id.entryAddFragment)

        }

        binding.ibProfile.setOnClickListener {

            binding.fragmentContainerView.findNavController().navigate(R.id.settingsFragment)

        }


        onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                binding.fragmentContainerView.findNavController().navigateUp()
            }
        })


    }
}