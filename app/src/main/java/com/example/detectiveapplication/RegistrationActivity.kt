package com.example.detectiveapplication

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.navigation.fragment.NavHostFragment
import com.example.detectiveapplication.databinding.ActivityRegistrationBinding
import okhttp3.internal.Util
import java.util.*

class RegistrationActivity : AppCompatActivity() {
    lateinit var navHostFragment: NavHostFragment
    private lateinit var binding : ActivityRegistrationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setLocate("ar")
        supportActionBar?.hide()

//        navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_registration) as NavHostFragment


    }

    private fun setLocate(Lang: String) {

        val locale = Locale(Lang)

        Locale.setDefault(locale)

        val config = Configuration()

        config.locale = locale
        baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)
    }
}