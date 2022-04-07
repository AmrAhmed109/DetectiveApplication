package com.example.detectiveapplication.feature.cases

import CasesAdapter
import CasesViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.englizya.common.base.BaseFragment
import com.example.detectiveapplication.R
import com.example.detectiveapplication.databinding.FragmentCasesBinding
import com.example.detectiveapplication.dto.cases.Case
import com.example.detectiveapplication.response.ActiveCasesResponse
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CasesFragment : BaseFragment() {

    private var _binding: FragmentCasesBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: CasesAdapter
    private val casesViewModel: CasesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCasesBinding.inflate(inflater, container, false)

        binding.cvFoundSomeone.setOnClickListener {
            findNavController().navigate(R.id.action_casesFragment_to_createStrangerCaseFragment)
        }

        binding.cvLostSomeone.setOnClickListener {
            findNavController().navigate(R.id.action_casesFragment_to_createParentCaseFragment)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupListeners()
        lifecycleScope.launchWhenCreated {
            casesViewModel.getCases()
        }
    }

    private fun setupObservers() {
        casesViewModel.cases.observe(viewLifecycleOwner) {
            updateUI(it)
        }
    }

    private fun updateUI(response: ActiveCasesResponse) {
        adapter = CasesAdapter(response.cases) {
            progressToCaseDetails(it)
        }

        binding.cases.adapter = adapter
    }

    private fun setupListeners() {
    }

    private fun progressToCaseDetails(case: Case) {

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}

