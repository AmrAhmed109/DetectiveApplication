package com.example.detectiveapplication.feature.signup

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.util.Patterns
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
import com.example.detectiveapplication.databinding.FragmentRegistrationBinding
import com.example.detectiveapplication.utils.NetworkResult
import com.maxkeppeler.sheets.info.InfoSheet


class RegistrationFragment : Fragment() {


    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding!!
    private lateinit var registrationViewModel: RegistrationViewModel
    val tage = "RegistrationFragment"

   private lateinit var dialogLoader: Dialogloader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registrationViewModel = ViewModelProvider(requireActivity())[RegistrationViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        dialogLoader = Dialogloader(requireContext())
        dialogLoader.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

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

        binding.btnSignUpButton.setOnClickListener {
            if (validateFormData()){
                requestApiData()
            }
        }
    }



    private fun validateFormData(): Boolean {
        var isValid = true
        var message = "الرجاء إدخال جميع الحقول"
        if (binding.etRewritePassword.text.toString() != binding.etPassword.text.toString()) {
            binding.etRewritePassword.error = "الرجاء التأكد من  تطابق كلمة السر"
            message = "الرجاء التأكد من  تطابق كلمة السر"
            isValid =  false
        }
        if (binding.etRewritePassword.text.toString().isEmpty()) {
            binding.etRewritePassword.error = "الرجاء إعادة كتابة كلمة السر"
            message = "الرجاء إعادة كتابة كلمة السر"
            isValid =  false
        }
        if (binding.etPassword.text.toString().isEmpty()) {
            binding.etPassword.error = "الرجاء إدخال كلمة السر"
            message = "الرجاء إدخال كلمة السر"
            isValid =  false
        }
//        if (validateEmail(binding.etEmail.text.toString())) {
//            binding.etEmail.error = "الرجاء إدخال ايميل صحيح"
//            message = "الرجاء إدخال ايميل صحيح"
//            isValid =  false
//        }
        if (binding.etEmail.text.toString().isEmpty()) {
            binding.etEmail.error = "الرجاء إدخال الايميل الخاص بك"
            message = "الرجاء إدخال الايميل الخاص بك"
            isValid =  false
        }
        if (binding.etSecondName.text.toString().isEmpty()) {
            binding.etSecondName.error = "الرجاء إدخال الاسم الاخير"
            message = "الرجاء إدخال الاسم الاخير"
            isValid =  false
        }
        if (binding.etFirstName.text.toString().isEmpty()) {
            binding.etFirstName.error = "الرجاء إدخال الاسم الاول"
            message = "الرجاء إدخال الاسم الاول"
            isValid =  false
        }
        if (!isValid){
            missingInputDialog(title = "تأكد من ملئ البيانات بشكل صحيح", message = message)
            return false
        }
        return true
    }
    fun missingInputDialog(title: String, message: String){
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
    private fun validateEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
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
                binding.etFirstName.text.toString() +" "+ binding.etSecondName.text.toString(),
                binding.etEmail.text.toString(),
                binding.etPassword.text.toString(),
                binding.etRewritePassword.text.toString()
            )
        )
        registrationViewModel.registrationResponse.observe(viewLifecycleOwner) { response ->

            when (response) {
                is NetworkResult.Success -> {
                    hideLoader()
                    response.data?.let { Toast.makeText(requireContext(), "created New User Named ${it.name}", Toast.LENGTH_LONG).show() }
                    findNavController().popBackStack()

                }
                is NetworkResult.Error -> {
                    hideLoader()
                    Toast.makeText(
                        requireContext(),
                        "Error ${response.message}",
                        Toast.LENGTH_LONG
                    ).show()
                    Log.d("NetworkResult.Error", "requestApiData: ${response.message}")
                }
                is NetworkResult.Loading -> {
                    showLoader()
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