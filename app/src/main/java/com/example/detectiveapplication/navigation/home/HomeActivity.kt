package com.example.detectiveapplication.navigation.home

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.detectiveapplication.R
import com.example.detectiveapplication.databinding.ActivityMainBinding
import com.example.detectiveapplication.feature.settings.SettingViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class HomeActivity : AppCompatActivity(), NavController.OnDestinationChangedListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var settingViewModel: SettingViewModel
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        setLocate("ar")
        settingViewModel = ViewModelProvider(this)[SettingViewModel::class.java]
        settingViewModel.saveLanguage()
        settingViewModel.language.observe(this) {
            setLocate(it)
        }
        updateLanguage()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        settingViewModel = ViewModelProvider(this)[SettingViewModel::class.java]
        settingViewModel.saveLanguage()
        settingViewModel.language.observe(this) {
            setLocate(it)
        }

        val sharedPref = this.getSharedPreferences("shared", Context.MODE_PRIVATE)
        val editor =sharedPref?.edit()
        editor?.apply {
            val language = "ar"
            putString("language",language)
            apply()
        }
        val languageReturned = sharedPref?.getString("language","ar")
        setLocate(languageReturned!!)
        updateLanguage()

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
    
    private fun updateLanguage() {
        val locale = Locale.Builder().setLanguage(settingViewModel.language.value).build()
        Locale.setDefault(locale)
        val resources: Resources = this.resources
        val config: Configuration = resources.configuration
        config.setLocale(locale)
        config.setLayoutDirection(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
        this.createConfigurationContext(config)
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