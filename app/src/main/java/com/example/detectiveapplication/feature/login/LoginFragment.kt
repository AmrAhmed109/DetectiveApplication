package com.example.detectiveapplication.feature.login

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.detectiveapplication.Dialogloader
import com.example.detectiveapplication.R
import com.example.detectiveapplication.databinding.FragmentLoginBinding
import com.example.detectiveapplication.navigation.home.HomeActivity
import com.example.detectiveapplication.utils.NetworkResult
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var loginViewModel: LoginViewModel
    val tage = "LoginFragment"
    private lateinit var dialogLoader: Dialogloader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dialogLoader = Dialogloader(requireContext())
        dialogLoader.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        loginViewModel = ViewModelProvider(requireActivity())[LoginViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvDontHaveAnAccount.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registrationFragment)
        }

        binding.tvForgetPassword.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_forgetPasswordFragment)
        }

        binding.btnSignInButton.setOnClickListener {
            requestApiData()
        }

    }

    private fun loginRequest(email: String, password: String): Map<String, String> {
        val map: HashMap<String, String> = HashMap()
        map["email"] = email
        map["password"] = password
        map["fcm_token"] = "123456"

        return map
    }

    fun showLoader() {
        dialogLoader.show()
        dialogLoader
    }

    fun hideLoader() {
        dialogLoader.hide()
    }

    fun addSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT)
            .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.green)).show()
    }

    fun addBadSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT)
            .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.red)).show()
    }

    private fun finishActivity() {
    }

    private fun requestApiData() {
        Log.v("recipesFragment", "requestApiData called!")
        loginViewModel.login(
            loginRequest(
                binding.etUsername.text?.trim().toString(),
                binding.etPasswordInput.text?.trim().toString()
            )
        )
        loginViewModel.loginResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    hideLoader()


//                    Intent(requireContext(), HomeActivity::class.java).also {
//                        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                        finish()
//                        startActivity(it)
//                    }

                    response.data?.let {
//                        addSnackbar("${response.data.data.userData.name}اهلا بك ")
                        addSnackbar(response.data.message)
                        Toast.makeText(requireContext(), response.data.message, Toast.LENGTH_LONG)
                            .show()
                    }
                    findNavController().navigate(R.id.action_loginFragment_to_homeActivity)
                    requireActivity().finish()
                }
                is NetworkResult.Error -> {
                    hideLoader()
                    addBadSnackbar(response.message.toString())
//                    Toast.makeText(requireContext(), response.message.toString(), Toast.LENGTH_LONG).show()
                    Log.d("NetworkResult.Error", "requestApiData: ${response.message.toString()}")
                }
                is NetworkResult.Loading -> {
                    showLoader()
//                    Toast.makeText(requireContext(), "Loading", Toast.LENGTH_LONG)
//                        .show()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}