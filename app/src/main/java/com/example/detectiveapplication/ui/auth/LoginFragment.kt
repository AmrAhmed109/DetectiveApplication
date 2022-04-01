package com.example.detectiveapplication.ui.auth

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.detectiveapplication.R
import com.example.detectiveapplication.databinding.FragmentLoginBinding
import com.example.detectiveapplication.utils.Constants
import com.example.detectiveapplication.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authViewModel = ViewModelProvider(requireActivity())[AuthViewModel::class.java]
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

    private fun requestApiData() {
        Log.v("recipesFragment", "requestApiData called!")
        val queries: HashMap<String, String> = HashMap()
        queries["email"] = "name@gmail.com"
        queries["password"] = "123456789"

        authViewModel.login(queries)
        authViewModel.loginResponse.observe(viewLifecycleOwner, { response ->

            when (response) {
                is NetworkResult.Success -> {
                    findNavController().navigate(R.id.action_loginFragment_to_homeActivity)
                    response.data?.let {
                        Toast.makeText(
                            requireContext(),
                            response.data.message,
                            Toast.LENGTH_LONG
                        )
                            .show()
                    }
                }
                is NetworkResult.Error -> {
                    Toast.makeText(requireContext(), response.message.toString(), Toast.LENGTH_LONG)
                        .show()
                    Log.d("NetworkResult.Error", "requestApiData: ${response.message.toString()}")
                }

                is NetworkResult.Loading -> {
                    Toast.makeText(requireContext(), "Loading", Toast.LENGTH_LONG)
                        .show()
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }



}