package com.example.detectiveapplication.feature.search

import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_SEARCH
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.detectiveapplication.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }
        openKeyboard()

        binding.searchEditText.setOnEditorActionListener { v, actionId, event ->
            if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH) {
                val query = binding.searchEditText.text.toString()
                hideKeyboard()
                binding.searchEditText.disableFocusOnEditText()
                if (query.isNotEmpty()) {
                    Toast.makeText(requireContext(), query, Toast.LENGTH_SHORT).show()
                    //TODO: search for query
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

    private fun EditText.disableFocusOnEditText() {
        this.isFocusable = false
        this.isFocusableInTouchMode = false
        this.isFocusable = true
        this.isFocusableInTouchMode = true
    }

    private fun hideKeyboard() {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
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


}