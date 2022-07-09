package com.example.detectiveapplication.feature.settings

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.detectiveapplication.Dialogloader
import com.example.detectiveapplication.R
import com.example.detectiveapplication.databinding.FragmentSettingBinding
import com.example.detectiveapplication.dto.profile_data.UserProfileInfo
import com.example.detectiveapplication.utils.NetworkResult
import com.maxkeppeler.sheets.info.InfoSheet
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingFragment : Fragment() {
    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!
    private lateinit var settingViewModel: SettingViewModel
    val tage = "SettingFragment"
    var userProfile:UserProfileInfo? = null
    private lateinit var dialogLoader: Dialogloader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        settingViewModel = ViewModelProvider(requireActivity())[SettingViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        dialogLoader = Dialogloader(requireContext())
        dialogLoader.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestApiData()

        binding.cvLogOut.setOnClickListener {
//            requestLogout()
           logOutDialog(title = "تسجيل خروج", message = "هل انت متأكد من تسجيل الخروج ؟")
        }
        binding.cvEditProfile.setOnClickListener {
            val action = SettingFragmentDirections.actionSettingFragmentToEditProfileFragment(userProfile)
            findNavController().navigate(action)
        }
        binding.cvWatingCases.setOnClickListener {
            findNavController().navigate(R.id.action_settingFragment_to_watingCasesFragment)
        }
    }

    fun logOutDialog(title: String, message: String){
         InfoSheet().show(requireActivity()) {
            title(title)
            content(message)
             onPositive("لا") {
                 // Handle event
             }
             onNegative("اجل") {
                 requestLogout()
            }
        }
    }
    fun showLoader(){
        dialogLoader.show()
        dialogLoader
    }
    fun hideLoader(){
        dialogLoader.hide()
    }
    private fun requestLogout() {
        Log.v(tage, "requestApiData called!")

        settingViewModel.userLogout()
        settingViewModel.logoutResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    hideLoader()
                    response.data?.let {
//                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_settingFragment_to_registrationActivity)
                        requireActivity().finish()
                        Log.d(tage, "Success : ${response.message.toString()}")
                    }
                }
                is NetworkResult.Error -> {
                    hideLoader()
                    Toast.makeText(requireContext(), response.message.toString(), Toast.LENGTH_LONG).show()
                    Log.d(tage, "Error 1 : ${response.message.toString()}")
                }
                is NetworkResult.Loading -> {
                    showLoader()
//                    Toast.makeText(requireContext(), "Loading", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun requestApiData() {
        Log.v(tage, "requestApiData called!")
        settingViewModel.getUserProfileInfo()
        settingViewModel.userInfoResponse.observe(viewLifecycleOwner) { response ->

            when (response) {
                is NetworkResult.Success -> {
                    hideLoader()
                    response.data?.let {
                        userProfile = it
//                        Toast.makeText(requireContext(), userProfile!!.email.toString(), Toast.LENGTH_LONG).show()
                        binding.tvName.text = it.name
                        binding.tvEmail.text = it.email
                    }
                }
                is NetworkResult.Error -> {
                    hideLoader()
                    Toast.makeText(requireContext(), response.message.toString(), Toast.LENGTH_LONG).show()
                    Log.d(tage, "Error 2: ${response.message.toString()}")
                }

                is NetworkResult.Loading -> {
                    showLoader()
//                    Toast.makeText(requireContext(), "Loading", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}