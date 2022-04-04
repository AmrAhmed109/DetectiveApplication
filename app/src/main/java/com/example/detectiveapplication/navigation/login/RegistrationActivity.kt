package com.example.detectiveapplication.navigation.login

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.detectiveapplication.R
import com.example.detectiveapplication.databinding.ActivityRegistrationBinding
import com.example.detectiveapplication.feature.login.LoginFragment
import com.example.detectiveapplication.feature.login.LoginViewModel
import com.example.detectiveapplication.repository.DataStoreRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.internal.Util
import java.util.*
@AndroidEntryPoint
class RegistrationActivity : AppCompatActivity() {
    private lateinit var binding : ActivityRegistrationBinding
    private lateinit var navHostFragment : NavHostFragment
    private lateinit var loginViewModel:LoginViewModel
    private val dataStoreRepository:DataStoreRepository by lazy {
        DataStoreRepository(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setLocate("ar")
        supportActionBar?.hide()
        navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_registration) as NavHostFragment
        val navController = navHostFragment.navController
        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        CoroutineScope(Dispatchers.Main).launch {
            dataStoreRepository.readToken.collect {
                Toast.makeText(this@RegistrationActivity,"saved token $it", Toast.LENGTH_SHORT).show()
                if (it.isNotEmpty()){
                    navController.navigate(R.id.action_loginFragment_to_homeActivity)
                }
            }
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