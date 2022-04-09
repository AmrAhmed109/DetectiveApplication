package com.example.detectiveapplication.navigation.login

import android.content.res.Configuration
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.example.detectiveapplication.R
import com.example.detectiveapplication.databinding.ActivityRegistrationBinding
import com.example.detectiveapplication.feature.login.LoginViewModel
import com.example.detectiveapplication.feature.settings.SettingViewModel
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setLocate("ar")
        supportActionBar?.hide()
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_registration) as NavHostFragment
        val navController = navHostFragment.navController
        settingViewModel = ViewModelProvider(this)[SettingViewModel::class.java]
        if (settingViewModel.getToken().isNotEmpty()){
            navController.navigate(R.id.action_loginFragment_to_homeActivity)
        }

    }

    private fun setLocate(Lang: String) {

        val locale = Locale(Lang)

        Locale.setDefault(locale)

        val config = Configuration()

        config.locale = locale
        baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)
    }
}