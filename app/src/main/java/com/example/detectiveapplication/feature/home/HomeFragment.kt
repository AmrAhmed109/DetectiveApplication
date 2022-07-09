package com.example.detectiveapplication.feature.home

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.detectiveapplication.Dialogloader
import com.example.detectiveapplication.R
import com.example.detectiveapplication.databinding.FragmentHomeBinding
import com.example.detectiveapplication.dto.cases.Case
import com.example.detectiveapplication.feature.home.utils.capitalList
import com.example.detectiveapplication.response.ActiveCasesResponse
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var adapter: CasesAdapter
    private lateinit var capitalAdapter: CapitalAdapter

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var dialogLoader: Dialogloader
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        dialogLoader = Dialogloader(requireContext())
        dialogLoader.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding.cvSearch.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToSearchFragment("","","","","")
            findNavController().navigate(action)
        }
        binding.cvNotification.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToNotificationFragment()
            findNavController().navigate(action)
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            lifecycleScope.launch {
                homeViewModel.getFeedCases()
            }
            homeViewModel.cases.observe(viewLifecycleOwner) {
                updateUI(it)
            }
            binding.swipeRefreshLayout.isRefreshing = false
        }

        homeViewModel.loading.observe(viewLifecycleOwner) { if (it) { showLoader() } else { hideLoader() } }
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
        super.onViewCreated(view, savedInstanceState)

        setupListeners()
        setupObservers()
        setupCapitalsUI()

        lifecycleScope.launch {
            homeViewModel.getFeedCases()
        }
    }

    private fun setupCapitalsUI() {
        capitalAdapter = CapitalAdapter(
            capitalList,
            homeViewModel.selectedCapital.value!!,
        ) {
            homeViewModel.setSelectedCapital(it)
        }

        binding.capitalRV.adapter = capitalAdapter
    }

    private fun setupObservers() {
        homeViewModel.error.observe(viewLifecycleOwner) {
            progressToLogin()
        }

        homeViewModel.cases.observe(viewLifecycleOwner) {

        }

        homeViewModel.cases.observe(viewLifecycleOwner) {
            updateUI(it)
        }

        homeViewModel.selectedCapital.observe(viewLifecycleOwner) {
            setupCapitalsUI()
        }
    }

    private fun progressToLogin() {
//        startActivity(
//            Intent(
//                context,
//                RegistrationActivity::class.java
//            )
//        )
//        activity?.finish()
    }

    private fun setupListeners() {
    }

    private fun updateUI(activeCasesResponse: ActiveCasesResponse?) {
        activeCasesResponse?.data?.let {
            adapter =
                CasesAdapter(it.cases) {
                    showCase(it)
                }

            binding.rvHome.adapter = adapter
        }!!
    }

    private fun showCase(it: Case) {
        val action = HomeFragmentDirections.actionHomeFragmentToDetailsFragment(it.id.toString())
        findNavController().navigate(action)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}