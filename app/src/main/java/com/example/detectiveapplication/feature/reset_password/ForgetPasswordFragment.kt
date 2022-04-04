package com.example.detectiveapplication.feature.reset_password

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
import com.example.detectiveapplication.databinding.FragmentForgetPasswordBinding
import com.example.detectiveapplication.databinding.FragmentRegistrationBinding
import com.example.detectiveapplication.feature.login.LoginViewModel
import com.example.detectiveapplication.utils.NetworkResult


class ForgetPasswordFragment : Fragment() {

    private var _binding: FragmentForgetPasswordBinding? = null
    private val binding get() = _binding!!
    private lateinit var resetPasswordViewModel: ResetPasswordViewModel
    val tage ="ForgetPasswordFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        resetPasswordViewModel = ViewModelProvider(requireActivity())[ResetPasswordViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentForgetPasswordBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnEmailCheck.setOnClickListener {
            requestApiData()

        }

    }

    private fun forgetPasswordRequest(email : String ):Map<String,String>{
        val map: HashMap<String, String> = HashMap()
        map["email"] = email
        return map
    }

    private fun requestApiData() {
        Log.v(tage, "requestApiData called!")

        resetPasswordViewModel.forgetPassword(forgetPasswordRequest("amro.ahmed1009@gmail.com"))
        resetPasswordViewModel.forgetPasswordResponse.observe(viewLifecycleOwner, { response ->

            when (response) {
                is NetworkResult.Success -> {
                    Log.v(tage, "findNavController")
                    response.data?.let {
                        findNavController().navigate(R.id.action_forgetPasswordFragment_to_emailCheckFragment)
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
                    Log.d(tage, "requestApiData: ${response.message.toString()}")
                }

                is NetworkResult.Loading -> {
                    Toast.makeText(requireContext(), "Loading", Toast.LENGTH_SHORT)
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