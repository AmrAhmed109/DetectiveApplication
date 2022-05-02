package com.example.detectiveapplication.ui.home

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
import com.example.detectiveapplication.R
import com.example.detectiveapplication.databinding.FragmentWatingCasesBinding
import com.example.detectiveapplication.dto.pendingCases.DataList
import com.example.detectiveapplication.ui.home.WatingCasesAdapter.Interaction
import com.example.detectiveapplication.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WatingCasesFragment : Fragment(), Interaction {

    private var _binding: FragmentWatingCasesBinding? = null
    private val binding get() = _binding!!
    private val watingCasesViewModel: WatingCasesViewModel by viewModels()
    private val watingCasesAdapter: WatingCasesAdapter by lazy {
        WatingCasesAdapter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWatingCasesBinding.inflate(layoutInflater, container, false)
        intiRecycler()
        apiRequest()
        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }

        return binding.root
    }

    private fun apiRequest() {
        watingCasesViewModel.getUserPendingCases()
        watingCasesViewModel.pendingCasesResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    response.data?.let {
                        Log.d("watingCasesAdapter", "here")
                        Log.d("watingCasesAdapter", it.data.data.toString())
                        watingCasesAdapter.submitList(it.data.data)

//                        Toast.makeText(requireContext(), it.code, Toast.LENGTH_SHORT).show()
                    }
                }
                is NetworkResult.Error -> {
                    Toast.makeText(requireContext(), response.message.toString(), Toast.LENGTH_LONG)
                        .show()
//                    Log.d(tage, "Error 1 : ${response.message.toString()}")
                }

                is NetworkResult.Loading -> {
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
        Toast.makeText(requireContext(), item.name, Toast.LENGTH_SHORT).show()
    }


}