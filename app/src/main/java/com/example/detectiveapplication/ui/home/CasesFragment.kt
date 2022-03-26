package com.example.detectiveapplication.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.detectiveapplication.R
import com.example.detectiveapplication.databinding.FragmentCasesBinding

class CasesFragment : Fragment() {

    private var _binding: FragmentCasesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCasesBinding.inflate(inflater,container,false)

        binding.cvFoundSomeone.setOnClickListener {
            findNavController().navigate(R.id.action_casesFragment_to_createStrangerCaseFragment)
        }

        binding.cvLostSomeone.setOnClickListener {
            findNavController().navigate(R.id.action_casesFragment_to_createParentCaseFragment)
        }

        return binding.root
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}

