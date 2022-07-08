package com.example.detectiveapplication.feature.search

import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_SEARCH
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.detectiveapplication.databinding.FragmentSearchBinding
import com.example.detectiveapplication.dto.search_response.SearchDataX
import com.example.detectiveapplication.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment(), SearchAdapter.Interaction {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var searchViewModel: SearchViewModel
    private val searchAdapter: SearchAdapter by lazy {
        SearchAdapter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        searchViewModel = ViewModelProvider(this)[SearchViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        intialRecycelerView()
        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }
        apiCall("")
        openKeyboard()
        binding.searchEditText.setOnEditorActionListener { v, actionId, event ->
            if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH) {
                val query = binding.searchEditText.text.toString()
                hideKeyboard()
                binding.searchEditText.disableFocusOnEditText()
                if (query.isNotEmpty()) {
                    Toast.makeText(requireContext(), query, Toast.LENGTH_SHORT).show()
                    apiCall(query)
                } else {
                    Toast.makeText(context, "Nothing To Search For", Toast.LENGTH_SHORT).show()
                }
                true
            } else {
                false
            }
        }
        return binding.root
    }

    fun apiCall(query: String) {
        searchViewModel.query(query)
        searchViewModel.searchResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    response.data?.let {
                        searchAdapter.submitList(it.data.data)
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

    fun intialRecycelerView() {
        binding.rvSearchedResult.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = searchAdapter
        }
    }

    private fun EditText.disableFocusOnEditText() {
        this.isFocusable = false
        this.isFocusableInTouchMode = false
        this.isFocusable = true
        this.isFocusableInTouchMode = true
    }

    private fun hideKeyboard() {
        val imm =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.searchEditText.windowToken, 0)
    }

    private fun openKeyboard() {
        binding.searchEditText.requestFocus()
        binding.searchEditText.postDelayed({
            val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(binding.searchEditText, 0)
        }, 200)
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onItemSelected(position: Int, item: SearchDataX, state: Int) {
        val action =
            SearchFragmentDirections.actionSearchFragmentToDetailsFragment(item.id.toString())
        findNavController().navigate(action)
    }


}