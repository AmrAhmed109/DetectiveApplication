package com.example.detectiveapplication.feature.cases

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
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
import com.example.detectiveapplication.feature.home.HomeFragmentDirections
import com.example.detectiveapplication.response.ActiveCasesResponse
import com.example.detectiveapplication.ui.home.FollowingFragmentDirections
import com.example.detectiveapplication.utils.NetworkResult
import com.maxkeppeler.sheets.info.InfoSheet
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
        binding.cvNotification.setOnClickListener {
            val action = CasesFragmentDirections.actionCasesFragmentToNotificationFragment()
            findNavController().navigate(action)
        }
        binding.cvSearch.setOnClickListener {
            val action = CasesFragmentDirections.actionCasesFragmentToSearchFragment("","","","","")
            findNavController().navigate(action)
        }
        binding.cvFoundSomeone.setOnClickListener {
            findNavController().navigate(R.id.action_casesFragment_to_createStrangerCaseFragment)
        }

        binding.cvLostSomeone.setOnClickListener {
            findNavController().navigate(R.id.action_casesFragment_to_createParentCaseFragment)
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            refreshData()
            binding.swipeRefreshLayout.isRefreshing = false
        }

        casesViewModel.loading.observe(viewLifecycleOwner) { if (it) { showLoader() } else { hideLoader() } }
        return binding.root
    }

    fun refreshData(){
        setupObservers()
        lifecycleScope.launchWhenCreated {
            casesViewModel.getCases()
        }
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

    fun apiCall(caseId:String) {
        casesViewModel.closeCase(caseId)
        casesViewModel.closedCaseResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    refreshData()
                    Toast.makeText(requireContext(), response.data?.message, Toast.LENGTH_LONG).show()
                }
                is NetworkResult.Error -> {
                    Toast.makeText(requireContext(), "Error ${response.message.toString()}", Toast.LENGTH_LONG).show()
                }
                is NetworkResult.Loading -> {
//                    Toast.makeText(requireContext(),"Loading", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    fun closeCaseDialog(title: String, message: String,id:String){
        InfoSheet().show(requireActivity()) {
            title(title)
            content(message)
            onPositive("لا") {
                // Handle event
            }
            onNegative("اجل") {
                apiCall(id)
            }
        }
    }
    override fun onFoundSelected(position: Int, case: Case, state: Int) {
        closeCaseDialog("هل تريد اغلاق القضية ؟","سيتم إغلاق القضية بنائا على طلبك",case.id.toString())
//        apiCall(case.id.toString())
//        Toast.makeText(requireContext(), "Found Selected For ${case.name}", Toast.LENGTH_SHORT).show()
    }

    override fun onDeleteSelected(position: Int, case: Case, state: Int) {
//        Toast.makeText(requireContext(), "Delete Selected For ${case.name}", Toast.LENGTH_SHORT).show()
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

