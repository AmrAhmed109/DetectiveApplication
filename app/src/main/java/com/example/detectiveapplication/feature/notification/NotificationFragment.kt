package com.example.detectiveapplication.feature.notification

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.detectiveapplication.Dialogloader
import com.example.detectiveapplication.databinding.FragmentNotificationBinding
import com.example.detectiveapplication.dto.notification.NotificationFeed
import com.example.detectiveapplication.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat

@AndroidEntryPoint
class NotificationFragment : Fragment(), NotificationAdapter.Interaction {

    private val tage = "NotificationFragment"
    private var _binding: FragmentNotificationBinding? = null
    private val binding get() = _binding!!
    private lateinit var dialogLoader: Dialogloader
    private lateinit var notificationViewModel: NotificationViewModel
    private val notificationAdapter: NotificationAdapter by lazy {
        NotificationAdapter(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        notificationViewModel = ViewModelProvider(this)[NotificationViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNotificationBinding.inflate(inflater, container, false)
        initializeRecyclerView()
        dialogLoader = Dialogloader(requireContext())
        dialogLoader.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        apiCall()
        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.swipeRefreshLayout2.setOnRefreshListener {
            apiCall()
            binding.swipeRefreshLayout2.isRefreshing = false
        }
        return binding.root
    }
    fun showLoader() {
        dialogLoader.show()
    }

    fun hideLoader() {
        dialogLoader.hide()
    }


    fun apiCall() {
        notificationViewModel.getNotification()
        notificationViewModel.notificationResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    hideLoader()
                    response.data?.let {
                       notificationAdapter.submitList(it.data)
                    }
                }
                is NetworkResult.Error -> {
                    hideLoader()
                    Toast.makeText(requireContext(), response.message.toString(), Toast.LENGTH_LONG).show()
                    Log.d(tage, "Error: ${response.message.toString()}")
                }

                is NetworkResult.Loading -> {
                    showLoader()
                }
            }
        }
    }

    private fun initializeRecyclerView() {
        binding.recyclerView.apply {
            adapter = notificationAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemSelected(position: Int, item: NotificationFeed, state: Int) {
        notificationViewModel.notificationRead(item.id.toString())
        val action = NotificationFragmentDirections.actionNotificationFragmentToDetailsFragment(item.refreanceId.toString())
        findNavController().navigate(action)

    }

}