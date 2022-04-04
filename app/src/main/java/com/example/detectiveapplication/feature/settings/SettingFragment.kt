package com.example.detectiveapplication.feature.settings

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.detectiveapplication.R
import com.example.detectiveapplication.databinding.FragmentSettingBinding
import com.example.detectiveapplication.feature.login.LoginViewModel
import com.example.detectiveapplication.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingFragment : Fragment() {
    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!
    private lateinit var settingViewModel: SettingViewModel
    val tage = "SettingFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        settingViewModel = ViewModelProvider(requireActivity())[SettingViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestApiData()

        binding.cvLogOut.setOnClickListener {
            requestLogout()
        }
    }

    private fun requestLogout() {
        Log.v(tage, "requestApiData called!")

        settingViewModel.userLogout()
        settingViewModel.logoutResponse.observe(viewLifecycleOwner, { response ->

            when (response) {
                is NetworkResult.Success -> {
                    response.data?.let {
                        Toast.makeText(requireContext(),it.message,Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_settingFragment_to_registrationActivity)
                        Log.d(tage, "Success : ${response.message.toString()}")
                    }
                }
                is NetworkResult.Error -> {
                    Toast.makeText(requireContext(), response.message.toString(), Toast.LENGTH_LONG)
                        .show()
                    Log.d(tage, "Error 1 : ${response.message.toString()}")
                }

                is NetworkResult.Loading -> {
                    Toast.makeText(requireContext(), "Loading", Toast.LENGTH_LONG)
                        .show()
                }
            }
        })
    }

    private fun requestApiData() {
        Log.v(tage, "requestApiData called!")

        settingViewModel.getUserProfileInfo()
        settingViewModel.userInfoResponse.observe(viewLifecycleOwner, { response ->

            when (response) {
                is NetworkResult.Success -> {
                    response.data?.let {
                        binding.tvName.text = it.name
                        binding.tvEmail.text = it.email
                    }
                }
                is NetworkResult.Error -> {
                    Toast.makeText(requireContext(), response.message.toString(), Toast.LENGTH_LONG)
                        .show()
                    Log.d(tage, "Error 2: ${response.message.toString()}")
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