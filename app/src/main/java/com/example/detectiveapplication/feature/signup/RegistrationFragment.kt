package com.example.detectiveapplication.feature.signup

import android.os.Bundle
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
import com.example.detectiveapplication.databinding.FragmentRegistrationBinding
import com.example.detectiveapplication.feature.login.LoginViewModel
import com.example.detectiveapplication.utils.NetworkResult


class RegistrationFragment : Fragment() {


    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding!!
    private lateinit var registrationViewModel: RegistrationViewModel
    val tage = "RegistrationFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registrationViewModel =
            ViewModelProvider(requireActivity())[RegistrationViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnSignUpButton.setOnClickListener {
            requestApiData()
        }
    }

    private fun registrationRequest(
        name: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Map<String, String> {
        val map: HashMap<String, String> = HashMap()

        map["name"] = name
        map["email"] = email
        map["password"] = password
        map["password_confirmation"] = confirmPassword
        return map
    }

    private fun requestApiData() {
        Log.v(tage, "requestApiData called!")

        registrationViewModel.register(
            registrationRequest(
                "Amro",
                "amtr3qo@gmail.com",
                "123456789",
                "123456789"
            )
        )
        registrationViewModel.registrationResponse.observe(viewLifecycleOwner, { response ->

            when (response) {
                is NetworkResult.Success -> {
                    findNavController().popBackStack()
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
                    Toast.makeText(
                        requireContext(),
                        response.data?.error?.first().toString(),
                        Toast.LENGTH_LONG
                    ).show()
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