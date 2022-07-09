package com.example.detectiveapplication.feature.edit_profile

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.detectiveapplication.Dialogloader
import com.example.detectiveapplication.R
import com.example.detectiveapplication.databinding.FragmentEditProfileBinding
import com.example.detectiveapplication.utils.NetworkResult
import com.google.android.material.snackbar.Snackbar
import com.maxkeppeler.sheets.info.InfoSheet
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class EditProfileFragment : Fragment() {
    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!
    private val editProfileViewModel: EditProfileViewModel by viewModels()
    private lateinit var dialogLoader: Dialogloader

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        dialogLoader = Dialogloader(requireContext())
        dialogLoader.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        arguments?.let {
            binding.etFirstName.setText(EditProfileFragmentArgs.fromBundle(it).userProfile?.name)
                ?: ""
            binding.etEmail.setText(EditProfileFragmentArgs.fromBundle(it).userProfile?.email) ?: ""
        }
        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.btnSaveChanges.setOnClickListener {
            if (validateFormData()) {
                apiRequest()
            }
        }
        return binding.root
    }

    fun showLoader() {
        dialogLoader.show()
    }

    fun hideLoader() {
        dialogLoader.hide()
    }

    private fun validateFormData(): Boolean {
        var isValid = true
        var message = "الرجاء إدخال جميع الحقول"
        if (binding.etConfirmPassword.text.toString() != binding.etPassword.text.toString()) {
            binding.etConfirmPassword.error = "الرجاء التأكد من  تطابق كلمة السر"
            message = "الرجاء التأكد من  تطابق كلمة السر"
            isValid = false
        }
        if (binding.etConfirmPassword.text.toString().isEmpty()) {
            binding.etConfirmPassword.error = "الرجاء إعادة كتابة كلمة السر"
            message = "الرجاء إعادة كتابة كلمة السر"
            isValid = false
        }
        if (binding.etPassword.text.toString().isEmpty()) {
            binding.etPassword.error = "الرجاء إدخال كلمة السر"
            message = "الرجاء إدخال كلمة السر"
            isValid = false
        }
//        if (validateEmail(binding.etEmail.text.toString())) {
//            binding.etEmail.error = "الرجاء إدخال ايميل صحيح"
//            message = "الرجاء إدخال ايميل صحيح"
//            isValid =  false
//        }
        if (binding.etEmail.text.toString().isEmpty()) {
            binding.etEmail.error = "الرجاء إدخال الايميل الخاص بك"
            message = "الرجاء إدخال الايميل الخاص بك"
            isValid = false
        }
        if (binding.etFirstName.text.toString().isEmpty()) {
            binding.etFirstName.error = "الرجاء إدخال الاسم الاول"
            message = "الرجاء إدخال الاسم الاول"
            isValid = false
        }
        if (!isValid) {
            missingInputDialog(title = "تأكد من ملئ البيانات بشكل صحيح", message = message)
            return false
        }
        return true
    }

    fun missingInputDialog(title: String, message: String) {
        InfoSheet().show(requireActivity()) {
            title(title)
            content(message)
            onPositive("") {
                // Handle event
            }
            onNegative("حسنا") {
                // Handle event
            }
        }
    }

    fun addSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT)
            .setBackgroundTint(ContextCompat.getColor(requireContext(),R.color.green)).show()
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
                    hideLoader()
                    response.data?.let {
                        addSnackbar(response.data.message)
//                        Toast.makeText(requireContext(),response.data.message , Toast.LENGTH_LONG).show()
                        findNavController().popBackStack()
                    }
                }
                is NetworkResult.Error -> {
                    hideLoader()
                    missingInputDialog(
                        title = "خطأ في الإتصال",
                        message = response.message.toString()
                    )

                    Toast.makeText(requireContext(), response.message.toString(), Toast.LENGTH_LONG).show()
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