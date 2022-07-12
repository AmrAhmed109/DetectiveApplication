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
import com.example.detectiveapplication.databinding.FragmentEmailCheckBinding
import com.example.detectiveapplication.utils.NetworkResult

class EmailCheckFragment : Fragment() {

    private var _binding: FragmentEmailCheckBinding? = null
    private val binding get() = _binding!!
    private lateinit var resetPasswordViewModel: ResetPasswordViewModel
    val tage = "EmailCheckFragment"
    private lateinit var dialogLoader: Dialogloader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        resetPasswordViewModel =
            ViewModelProvider(requireActivity())[ResetPasswordViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEmailCheckBinding.inflate(inflater, container, false)
        dialogLoader = Dialogloader(requireContext())
        dialogLoader.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnEmailCheckCode.setOnClickListener {
            requestApiData()
        }

    }
    fun showLoader(){
        dialogLoader.show()
    }
    fun hideLoader(){
        dialogLoader.hide()
    }
    private fun checkCodeRequest(code: String): Map<String, String> {
        val map: HashMap<String, String> = HashMap()
        map["code"] = code
        return map
    }

    private fun requestApiData() {
        Log.v(tage, "requestApiData called!")

        resetPasswordViewModel.codeCheck(checkCodeRequest(binding.etCheckCode.text.toString()))
        resetPasswordViewModel.checkCodeResponse.observe(viewLifecycleOwner) { response ->

            when (response) {
                is NetworkResult.Success -> {
                    Log.v(tage, "findNavController")
                    hideLoader()
                    response.data?.let {
                        val action = EmailCheckFragmentDirections.actionEmailCheckFragmentToCreateNewPasswordFragment(binding.etCheckCode.text.toString())
                        findNavController().navigate(action)
                        Toast.makeText(requireContext(), response.data.message, Toast.LENGTH_LONG).show() }
                }
                is NetworkResult.Error -> {
                    hideLoader()
                    Toast.makeText(requireContext(), response.message.toString(), Toast.LENGTH_SHORT).show()
//                    val action = EmailCheckFragmentDirections.actionEmailCheckFragmentToCreateNewPasswordFragment(binding.etCheckCode.text.toString())
//                    findNavController().navigate(action)
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