package com.example.detectiveapplication.navigation.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.detectiveapplication.R
import com.example.detectiveapplication.databinding.ActivityMainBinding

class HomeActivity : AppCompatActivity(), NavController.OnDestinationChangedListener {

    private lateinit var binding : ActivityMainBinding
    private lateinit var navHostFragment : NavHostFragment
    private lateinit var appBarConfiguration: AppBarConfiguration
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_home) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNavigationView.setupWithNavController(navController)

        appBarConfiguration = AppBarConfiguration(setOf(
        R.id.homeFragment,
        R.id.followingFragment,
        R.id.casesFragment
        )
        )

        navController.addOnDestinationChangedListener(this)
        setupActionBarWithNavController(navController,appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navHostFragment.navController.navigateUp()||super.onSupportNavigateUp()
    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        destination?.let {
            when (destination.id) {
                R.id.searchFragment -> { binding.bottomNavigationView.visibility = View.GONE }
                R.id.createStrangerCaseFragment -> { binding.bottomNavigationView.visibility = View.GONE }
                R.id.createParentCaseFragment -> { binding.bottomNavigationView.visibility = View.GONE }
                else -> { binding.bottomNavigationView.visibility = View.VISIBLE }
            }
        }

    }


}