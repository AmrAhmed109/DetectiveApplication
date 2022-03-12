package com.example.detectiveapplication.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.detectiveapplication.R
import com.example.detectiveapplication.databinding.FragmentCreateParentCaseBinding
import com.example.detectiveapplication.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater,container,false)
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