package com.example.detectiveapplication.feature.reset_password

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.detectiveapplication.R
import com.example.detectiveapplication.databinding.FragmentEmailCheckBinding
import com.example.detectiveapplication.databinding.FragmentForgetPasswordBinding

class EmailCheckFragment : Fragment() {

    private var _binding: FragmentEmailCheckBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEmailCheckBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnEmailCheckCode.setOnClickListener {
            findNavController().navigate(R.id.action_emailCheckFragment_to_createNewPasswordFragment)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}