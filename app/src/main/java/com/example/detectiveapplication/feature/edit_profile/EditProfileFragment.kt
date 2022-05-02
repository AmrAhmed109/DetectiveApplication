package com.example.detectiveapplication.feature.edit_profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.detectiveapplication.R
import com.example.detectiveapplication.databinding.FragmentEditProfileBinding
import com.example.detectiveapplication.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class EditProfileFragment : Fragment() {
    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!
    private val editProfileViewModel: EditProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)

        arguments?.let {
            binding.etFirstName.setText(EditProfileFragmentArgs.fromBundle(it).userProfile?.name)?:""
            binding.etEmail.setText(EditProfileFragmentArgs.fromBundle(it).userProfile?.email)?:""
        }
        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.btnSaveChanges.setOnClickListener {
            apiRequest()
        }
        return binding.root
    }

    private fun apiRequest() {
        editProfileViewModel.editProfile(
            name = binding.etFirstName.text.toString(),
            email = binding.etEmail.text.toString(),
            password = binding.etPassword.text.toString(),
            confirm = binding.etConfirmPassword.text.toString()
        )
        editProfileViewModel.profileResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    response.data?.let {
                        Toast.makeText(
                            requireContext(),
                            response.data.message,
                            Toast.LENGTH_LONG
                        )
                            .show()
                        findNavController().popBackStack()
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
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}