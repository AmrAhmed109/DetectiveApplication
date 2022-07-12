package com.example.detectiveapplication.navigation.login

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.example.detectiveapplication.R
import com.example.detectiveapplication.databinding.ActivityRegistrationBinding
import com.example.detectiveapplication.feature.login.LoginViewModel
import com.example.detectiveapplication.feature.settings.SettingViewModel
import com.rbddevs.splashy.Splashy
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class RegistrationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegistrationBinding
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var settingViewModel: SettingViewModel

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
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        settingViewModel = ViewModelProvider(this)[SettingViewModel::class.java]
        settingViewModel.saveLanguage()
        settingViewModel.language.observe(this) {
            setLocate(it)
        }
        val sharedPref = this.getSharedPreferences("shared", Context.MODE_PRIVATE)
        val editor = sharedPref?.edit()
        editor?.apply {
            val language = "ar"
            putString("language", language)
            apply()
        }
        val languageReturned = sharedPref?.getString("language", "ar")
        setLocate(languageReturned!!)
        updateLanguage()
        supportActionBar?.hide()
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_registration) as NavHostFragment
        val navController = navHostFragment.navController
        Log.v("tokenCheck", settingViewModel.getToken())
        if (settingViewModel.getToken().length > 5) {
//            Splashy.hide()
            navController.navigate(R.id.action_loginFragment_to_homeActivity)
            this.finish()
        }
    }

    fun setSplashy() {
        Splashy(this)         // For JAVA : new Splashy(this)
            .setLogo(R.drawable.splashy)
            .setTitle("Splashy")
            .setTitleColor("#FFFFFF")
            .setSubTitle("Splash screen made easy")
            .setProgressColor(R.color.white)
            .setBackgroundResource(ContextCompat.getColor(this, R.color.white))
            .setFullScreen(true)
            .setTime(5000)
            .show()
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
}