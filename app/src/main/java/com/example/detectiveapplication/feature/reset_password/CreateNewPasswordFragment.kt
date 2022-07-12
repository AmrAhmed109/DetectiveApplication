package com.example.detectiveapplication.feature.reset_password

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
import com.example.detectiveapplication.databinding.FragmentCreateNewPasswordBinding
import com.example.detectiveapplication.utils.NetworkResult


class CreateNewPasswordFragment : Fragment() {


    private var _binding: FragmentCreateNewPasswordBinding? = null
    private val binding get() = _binding!!
    private var token: String? = null
    private lateinit var resetPasswordViewModel: ResetPasswordViewModel
    val tage = "CreateNewPasswordFragment"
    private lateinit var dialogLoader: Dialogloader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        resetPasswordViewModel = ViewModelProvider(requireActivity())[ResetPasswordViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreateNewPasswordBinding.inflate(inflater, container, false)
        dialogLoader = Dialogloader(requireContext())
        dialogLoader.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        arguments?.let {
            token = CreateNewPasswordFragmentArgs.fromBundle(it).token
        }
        return binding.root
    }
    fun showLoader(){
        dialogLoader.show()
    }
    fun hideLoader(){
        dialogLoader.hide()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.btnChangePassword.setOnClickListener {
            requestApiData()
        }

    }

    private fun resetPasswordRequest(
        password: String,
        confirmationPassword: String
    ): Map<String, String> {
        val map: HashMap<String, String> = HashMap()
        map["token"] = token.toString()
        map["password"] = password
        map["password_confirmation"] = confirmationPassword
        return map
    }

    private fun requestApiData() {
        Log.v(tage, "requestApiData called!")

        resetPasswordViewModel.resetPassword(
            resetPasswordRequest(
                binding.etPassword.text.toString(),
                binding.etRewritePassword.text.toString()
            )
        )
        resetPasswordViewModel.resetPasswordResponse.observe(viewLifecycleOwner) { response ->

            when (response) {
                is NetworkResult.Success -> {
                    hideLoader()
                    Log.v(tage, "findNavController")
                    response.data?.let {
                        findNavController().navigate(R.id.action_createNewPasswordFragment_to_loginFragment)
                        Toast.makeText(requireContext(), response.data.message, Toast.LENGTH_LONG).show()
                    }
                }
                is NetworkResult.Error -> {
                    hideLoader()
                    Toast.makeText(requireContext(), response.message.toString(), Toast.LENGTH_SHORT).show()
                    Log.d(tage, "requestApiData: ${response.message.toString()}")
                }
                is NetworkResult.Loading -> {
                    showLoader()
//                    Toast.makeText(requireContext(), "Loading", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}