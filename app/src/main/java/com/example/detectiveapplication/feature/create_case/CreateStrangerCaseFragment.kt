package com.example.detectiveapplication.feature.create_case

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.detectiveapplication.R
import com.example.detectiveapplication.databinding.FragmentCreateParentCaseBinding
import com.example.detectiveapplication.databinding.FragmentCreateStrangerCaseBinding


class CreateStrangerCaseFragment : Fragment() {
    private var _binding: FragmentCreateStrangerCaseBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreateStrangerCaseBinding.inflate(inflater,container,false)
        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }
        return binding.root
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}