package com.example.detectiveapplication.navigation.home

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.detectiveapplication.R
import com.example.detectiveapplication.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class HomeActivity : AppCompatActivity(), NavController.OnDestinationChangedListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var appBarConfiguration: AppBarConfiguration
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        setLocate("ar")
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val sharedPref = this.getSharedPreferences("shared", Context.MODE_PRIVATE)
        val editor =sharedPref?.edit()
        editor?.apply {
            val language = "ar"
            putString("language",language)
            apply()
        }
        val languageReturned = sharedPref?.getString("language","ar")
        setLocate(languageReturned!!)


        navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_home) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNavigationView.setupWithNavController(navController)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment,
                R.id.followingFragment,
                R.id.casesFragment,
                R.id.faceDetectionFragment
            )
        )

        navController.addOnDestinationChangedListener(this)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navHostFragment.navController.navigateUp() || super.onSupportNavigateUp()
    }


    private fun setLocate(Lang: String) {
        val locale = Locale(Lang)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)
    }
    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        destination.let {
            when (destination.id) {
                R.id.searchFragment -> {
                    binding.bottomNavigationView.visibility = View.GONE
                }
                R.id.createStrangerCaseFragment -> {
                    binding.bottomNavigationView.visibility = View.GONE
                }
                R.id.createParentCaseFragment -> {
                    binding.bottomNavigationView.visibility = View.GONE
                }
                R.id.editProfileFragment -> {
                    binding.bottomNavigationView.visibility = View.GONE
                }
                R.id.watingCasesFragment -> {
                    binding.bottomNavigationView.visibility = View.GONE
                }
                R.id.detailsFragment -> {
                    binding.bottomNavigationView.visibility = View.GONE
                }
                R.id.resultFragment -> {
                    binding.bottomNavigationView.visibility = View.GONE
                }
                R.id.notificationFragment -> {
                    binding.bottomNavigationView.visibility = View.GONE
                }
                R.id.filterFragment -> {
                    binding.bottomNavigationView.visibility = View.GONE
                }
                else -> {
                    binding.bottomNavigationView.visibility = View.VISIBLE
                }
            }
        }

    }


}