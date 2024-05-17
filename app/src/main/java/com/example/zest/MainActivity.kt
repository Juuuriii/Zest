package com.example.zest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.zest.databinding.ActivityMainBinding


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
                }


                R.id.searchFragment -> {
                    binding.bottomNav.visibility = View.VISIBLE
                    binding.floatingActionButton.visibility = View.VISIBLE
                }

                R.id.journalFragment -> {
                    binding.bottomNav.visibility = View.VISIBLE
                    binding.floatingActionButton.visibility = View.VISIBLE
                }

                R.id.calenderFragment -> {
                    binding.bottomNav.visibility = View.VISIBLE
                    binding.floatingActionButton.visibility = View.VISIBLE
                }

                else -> {
                    binding.bottomNav.visibility = View.GONE
                    binding.floatingActionButton.visibility = View.GONE
                }

            }
        }

        binding.floatingActionButton.setOnClickListener {

            navHost.findNavController().navigate(R.id.entryAddFragment)

        }
    }
}