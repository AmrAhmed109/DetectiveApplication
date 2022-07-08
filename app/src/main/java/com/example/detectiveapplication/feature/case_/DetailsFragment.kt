package com.example.detectiveapplication.feature.case_

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.detectiveapplication.Dialogloader
import com.example.detectiveapplication.R
import com.example.detectiveapplication.databinding.FragmentDetailsBinding
import com.example.detectiveapplication.utils.NetworkResult


class DetailsFragment : Fragment() {
    private val tage = "FollowingFragment"

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var caseDetailsViewModel: CaseDetailsViewModel
    private var phoneNumber: String? = null

    //    private var kidID :String? = null
    private lateinit var dialogLoader: Dialogloader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        caseDetailsViewModel =
            ViewModelProvider(requireActivity())[CaseDetailsViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        dialogLoader = Dialogloader(requireContext())
        dialogLoader.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


        arguments?.let {
            val kidID = DetailsFragmentArgs.fromBundle(it).id
            Log.d("kidID", "onCreateView: $kidID")
            requestApi(kidID!!)
        }
        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }

        observeViewModel()
        return binding.root
    }

    private fun requestApi(id: String) {
        Log.v(tage, "requestApi called!")
        caseDetailsViewModel.getDetailCasesInfo(id)
    }
    fun showLoader(){
        dialogLoader.show()
    }
    fun hideLoader(){
        dialogLoader.hide()
    }
    private fun observeViewModel() {
        caseDetailsViewModel.caseDetailResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    hideLoader()
                    Log.d(tage, "observeViewModel 2: we are here")
                    response.data?.let {
                        Log.d(tage, "observeViewModel 3: ${it.data.name}")
                        try {
                            binding.tvNameMissingChild.setText(it.data.name)
                            binding.tvAgeMissingChild.setText(" ${it.data.age} " + "سنة ")
                            binding.tvDateMissingChild.setText(it.data.kidnapDate)
                            binding.tvCityMissingChildd.setText(it.data.city)
                            binding.tvDescriptionMissingChild.setText(it.data.otherInfo)
                            binding.ivMissingChild.load(it.data.image)
                            handleButton(it.data.authFollowed, it.data.id.toString())
                            phoneNumber = it.data.guardian.first().phoneNumber
                        } catch (e: Exception) {
                            Log.d(tage, "observeViewModel 4: ${e.message}")
                        }
//                        Toast.makeText(requireContext(), "success", Toast.LENGTH_SHORT).show()
                        Log.d(tage, "observeViewModel 5: ${it.data.name}")
                    }
                }
                is NetworkResult.Error -> {
                    hideLoader()
                    Toast.makeText(requireContext(), response.message.toString(), Toast.LENGTH_LONG)
                        .show()
                    Log.d(tage, "Error 1 : ${response.message.toString()}")
                }

                is NetworkResult.Loading -> {
                    showLoader()
                    Toast.makeText(requireContext(), "Loading", Toast.LENGTH_LONG)
                        .show()
                }
                else -> {
                    Log.d(tage, "observeViewModel 5: ")

                }
            }
        }
    }

    fun String?.fixHttpsRequest(): String? {
        return this?.replace("http", "https")
    }

    private fun handleButton(status: Boolean, id: String) {
        binding.button.visibility = View.VISIBLE
        when (status) {
            true -> {
                binding.button.apply {
                    setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.purple_200))
                    text = "إلغاء المتابعة"
                    setOnClickListener {
                        removeRequestApi(id)

                    }

                }
            }
            false -> {
                binding.button.apply {
                    // kid is deleted state
                    setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.dark_grey))
                    text = "متابعة"
                    setOnClickListener {
                        addRequestApi(id)

                    }
                }
            }
        }

    }

    private fun addRequestApi(id: String) {
        Log.v(tage, "addRequestApi called!")

        caseDetailsViewModel.addKidInfo(id)
        caseDetailsViewModel.addKidResponse.observe(viewLifecycleOwner) { response ->

            when (response) {
                is NetworkResult.Success -> {
                    requestApi(id)
                    binding.button.apply {
                        setBackgroundColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.purple_200
                            )
                        )
                        text = "إلغاء المتابعة"
                    }

                    response.data?.let {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                }
                is NetworkResult.Error -> {
                    Toast.makeText(requireContext(), response.message.toString(), Toast.LENGTH_LONG)
                        .show()
                    Log.d(tage, "Error 1 : ${response.message.toString()}")
                }

                is NetworkResult.Loading -> {
                    Toast.makeText(requireContext(), "Loading", Toast.LENGTH_LONG)
                        .show()
                }
            }
        }

    }

    private fun removeRequestApi(id: String) {
        Log.v(tage, "removeRequestApi called!")

        caseDetailsViewModel.removeKidInfo(id)
        caseDetailsViewModel.removeKidResponse.observe(viewLifecycleOwner) { response ->

            when (response) {
                is NetworkResult.Success -> {
                    response.data?.let {
                        requestApi(id)
                        binding.button.apply {
                            setBackgroundColor(
                                ContextCompat.getColor(
                                    requireContext(),
                                    R.color.dark_grey
                                )
                            )
                            text = "متابعة"
                        }
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                }
                is NetworkResult.Error -> {
                    Toast.makeText(requireContext(), response.message.toString(), Toast.LENGTH_LONG)
                        .show()
                    Log.d(tage, "Error 1 : ${response.message.toString()}")
                }

                is NetworkResult.Loading -> {
                    Toast.makeText(requireContext(), "Loading", Toast.LENGTH_LONG)
                        .show()
                }
            }
        }

    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}