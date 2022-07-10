package com.example.detectiveapplication.ui.home

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.detectiveapplication.Dialogloader
import com.example.detectiveapplication.R
import com.example.detectiveapplication.databinding.FragmentFollowingBinding
import com.example.detectiveapplication.dto.followedCases.FollowedCasesItem
import com.example.detectiveapplication.feature.cases.CasesFragmentDirections
import com.example.detectiveapplication.feature.home.HomeFragmentDirections
import com.example.detectiveapplication.feature.settings.SettingViewModel
import com.example.detectiveapplication.utils.NetworkResult

class FollowingFragment : Fragment(),FollowingAdapter.Interaction {

    private val tage= "FollowingFragment"
    private var _binding: FragmentFollowingBinding? = null
    private val binding get() = _binding!!
    private lateinit var followingViewModel: FollowingViewModel
    private val followingAdapter:FollowingAdapter by lazy {
        FollowingAdapter(this)
    }
    private lateinit var dialogLoader: Dialogloader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        followingViewModel = ViewModelProvider(requireActivity())[FollowingViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFollowingBinding.inflate(inflater, container, false)
        dialogLoader = Dialogloader(requireContext())
        dialogLoader.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        requestApi()
        binding.cvSearch.setOnClickListener {
            val action = FollowingFragmentDirections.actionFollowingFragmentToSearchFragment("","","","","")
            findNavController().navigate(action)
        }
        binding.cvNotification.setOnClickListener {
            val action = FollowingFragmentDirections.actionFollowingFragmentToNotificationFragment()
            findNavController().navigate(action)
        }
        binding.rvFollowing.apply {
            adapter = followingAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            requestApi()
            binding.swipeRefreshLayout.isRefreshing = false
        }
        return binding.root
    }
    fun showLoader(){
        dialogLoader.show()
    }

    fun hideLoader(){
        dialogLoader.hide()
    }
    private fun requestApi() {
        Log.v(tage, "requestApiData called!")

        followingViewModel.getFollowedCasesInfo()
        followingViewModel.followedCasesResponse.observe(viewLifecycleOwner) { response ->

            when (response) {
                is NetworkResult.Success -> {
                    response.data?.let {
                        hideLoader()
                        followingAdapter.submitList(it)
//                        Toast.makeText(requireContext(), "success", Toast.LENGTH_SHORT).show()
                    }
                }
                is NetworkResult.Error -> {
                    hideLoader()
                    Toast.makeText(requireContext(), response.message.toString(), Toast.LENGTH_LONG).show()
                    Log.d(tage, "Error 1 : ${response.message.toString()}")
                }

                is NetworkResult.Loading -> {
                    showLoader()
//                    Toast.makeText(requireContext(), "Loading", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
    override fun onItemSelected(position: Int, item: FollowedCasesItem, state: Int) {
        val action = FollowingFragmentDirections.actionFollowingFragmentToDetailsFragment(item.id.toString())
        findNavController().navigate(action)
    }

}