package com.example.detectiveapplication.ui.home.watingCases

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.detectiveapplication.Dialogloader
import com.example.detectiveapplication.databinding.FragmentWatingCasesBinding
import com.example.detectiveapplication.dto.pendingCases.DataList
import com.example.detectiveapplication.ui.home.FollowingFragmentDirections
import com.example.detectiveapplication.ui.home.watingCases.WatingCasesAdapter.Interaction
import com.example.detectiveapplication.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WatingCasesFragment : Fragment(), Interaction {

    private var _binding: FragmentWatingCasesBinding? = null
    private val binding get() = _binding!!
    private val watingCasesViewModel: WatingCasesViewModel by viewModels()
    private lateinit var dialogLoader: Dialogloader
    private val watingCasesAdapter: WatingCasesAdapter by lazy {
        WatingCasesAdapter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWatingCasesBinding.inflate(layoutInflater, container, false)
        dialogLoader = Dialogloader(requireContext())
        dialogLoader.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        intiRecycler()
        apiRequest()
        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.swipeRefreshLayout.setOnRefreshListener {
            apiRequest()
            binding.swipeRefreshLayout.isRefreshing = false
        }
        return binding.root
    }
    fun showLoader(){
        dialogLoader.show()
        dialogLoader
    }
    fun hideLoader(){
        dialogLoader.hide()
    }
    private fun apiRequest() {
        watingCasesViewModel.getUserPendingCases()
        watingCasesViewModel.pendingCasesResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    hideLoader()
                    response.data?.let {
                        Log.d("watingCasesAdapter", "here")
                        Log.d("watingCasesAdapter", it.data.data.toString())
                        watingCasesAdapter.submitList(it.data.data)
                    }
                }
                is NetworkResult.Error -> {
                    hideLoader()
                    Toast.makeText(requireContext(), response.message.toString(), Toast.LENGTH_LONG).show()
                }

                is NetworkResult.Loading -> {
                    showLoader()
                    Toast.makeText(requireContext(), "Loading", Toast.LENGTH_LONG)
                        .show()
                }
            }


        }
    }

    fun intiRecycler() {
        binding.rvWatingCases.apply {
            adapter = watingCasesAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onItemSelected(position: Int, item: DataList, state: Int) {
        val action = WatingCasesFragmentDirections.actionWatingCasesFragmentToDetailsFragment(item.id.toString())
        findNavController().navigate(action)    }


}