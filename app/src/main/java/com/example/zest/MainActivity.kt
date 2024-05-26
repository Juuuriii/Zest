package com.example.zest

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.zest.databinding.ActivityMainBinding
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val navHost =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        binding.bottomNav.setupWithNavController(navHost.navController)

        navHost.navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment -> {
                    binding.bottomNav.visibility = View.VISIBLE
                    binding.floatingActionButton.visibility = View.VISIBLE
                    this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
                }


                R.id.searchFragment -> {
                    binding.bottomNav.visibility = View.VISIBLE
                    binding.floatingActionButton.visibility = View.VISIBLE
                    this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)

                }

                R.id.journalFragment -> {
                    binding.bottomNav.visibility = View.VISIBLE
                    binding.floatingActionButton.visibility = View.VISIBLE
                    this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
                }

                R.id.calenderFragment -> {
                    binding.bottomNav.visibility = View.VISIBLE
                    binding.floatingActionButton.visibility = View.VISIBLE
                    this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
                }

                R.id.signupFragment -> {

                    binding.bottomNav.visibility = View.GONE
                    binding.floatingActionButton.visibility = View.GONE
                    this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)


                }

                R.id.loginFragment -> {

                    binding.bottomNav.visibility = View.GONE
                    binding.floatingActionButton.visibility = View.GONE
                    this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)


                }

                else -> {
                    binding.bottomNav.visibility = View.GONE
                    binding.floatingActionButton.visibility = View.GONE
                    this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
                }

            }
        }

        binding.floatingActionButton.setOnClickListener {

            navHost.findNavController().navigate(R.id.entryAddFragment)

        }




    }
}