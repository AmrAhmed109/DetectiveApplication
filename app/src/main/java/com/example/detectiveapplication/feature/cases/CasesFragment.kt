package com.example.detectiveapplication.feature.cases

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.englizya.common.base.BaseFragment
import com.example.detectiveapplication.Dialogloader
import com.example.detectiveapplication.R
import com.example.detectiveapplication.databinding.FragmentCasesBinding
import com.example.detectiveapplication.dto.cases.Case
import com.example.detectiveapplication.response.ActiveCasesResponse
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CasesFragment : BaseFragment(), CasesAdapter.Interaction {

    private var _binding: FragmentCasesBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: CasesAdapter
    private lateinit var casesViewModel: CasesViewModel

    private lateinit var dialogLoader: Dialogloader
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCasesBinding.inflate(inflater, container, false)
        dialogLoader = Dialogloader(requireContext())
        dialogLoader.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        casesViewModel = ViewModelProvider(requireActivity())[CasesViewModel::class.java]

        binding.cvSearch.setOnClickListener {
            findNavController().navigate(R.id.action_casesFragment_to_searchFragment)
        }
        binding.cvFoundSomeone.setOnClickListener {
            findNavController().navigate(R.id.action_casesFragment_to_createStrangerCaseFragment)
        }

        binding.cvLostSomeone.setOnClickListener {
            findNavController().navigate(R.id.action_casesFragment_to_createParentCaseFragment)
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            setupObservers()
            lifecycleScope.launchWhenCreated {
                casesViewModel.getCases()
            }
            binding.swipeRefreshLayout.isRefreshing = false
        }

        casesViewModel.loading.observe(viewLifecycleOwner) { if (it) { showLoader() } else { hideLoader() } }
        return binding.root
    }

    fun showLoader(){
        dialogLoader.show()
        dialogLoader
    }
    fun hideLoader(){
        dialogLoader.hide()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupListeners()
        setupObservers()
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
        adapter = CasesAdapter(response.data.cases,this){
            progressToCaseDetails(it)
        }
    binding.cases.adapter = adapter
    }

    override fun onFoundSelected(position: Int, case: Case, state: Int) {
        Toast.makeText(requireContext(), "Found Selected For ${case.name}", Toast.LENGTH_SHORT).show()
    }

    override fun onDeleteSelected(position: Int, case: Case, state: Int) {
        Toast.makeText(requireContext(), "Delete Selected For ${case.name}", Toast.LENGTH_SHORT).show()
    }

    private fun setupListeners() {
    }

    private fun progressToCaseDetails(case: Case) {
        val action = CasesFragmentDirections.actionCasesFragmentToDetailsFragment(case.id.toString())
        findNavController().navigate(action)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}

