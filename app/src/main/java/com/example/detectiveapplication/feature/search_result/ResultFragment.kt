package com.example.detectiveapplication.feature.search_result

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.detectiveapplication.databinding.FragmentDetailsBinding
import com.example.detectiveapplication.databinding.FragmentResultBinding
import com.example.detectiveapplication.dto.recognition.RecognitionData


class ResultFragment : Fragment(), ResultAdapter.Interaction {

    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!
    private val resultAdapter : ResultAdapter by lazy {
        ResultAdapter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        binding.back.setOnClickListener {
            findNavController().navigateUp()
        }

        intiatRecyclerVIew()
        var data : List<RecognitionData>? = null
        arguments?.let {
             data = ResultFragmentArgs.fromBundle(it).recognitionResponse?.data
        }

        data?.let { it1 -> resultAdapter.submitList(it1) }




        return binding.root
    }

    fun intiatRecyclerVIew(){
        binding.rvResult.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = resultAdapter
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onItemSelected(position: Int, item: RecognitionData, state: Int) {
        val action = ResultFragmentDirections.actionResultFragmentToDetailsFragment(item.id.toString())
        findNavController().navigate(action)
    }
}