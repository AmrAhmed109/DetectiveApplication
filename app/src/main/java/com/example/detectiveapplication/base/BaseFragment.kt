package com.englizya.common.base

import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.detectiveapplication.base.network.ConnectionLiveData
import com.example.detectiveapplication.base.ui.LoadingDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
open class BaseFragment : Fragment() {

    lateinit var connectionLiveData: ConnectionLiveData

    val TAG = this::class.java.name
    private val loadingDialog: LoadingDialog by lazy {
        LoadingDialog(requireContext())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        connectionLiveData = ConnectionLiveData(requireContext())
    }

    fun handleLoading(state: Boolean) {
        if (state) {
            showLoading()
        } else {
            dismissLoading()
        }
    }

    private fun showLoading() {
        loadingDialog.show()
    }

    private fun dismissLoading() {
        loadingDialog.dismiss()
    }

    fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun showToast(messageRes: Int) {
        Toast.makeText(context, getText(messageRes), Toast.LENGTH_SHORT).show()
    }

    fun hideSoftKeyboard() {
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    }

    fun handleFailure(exception: Exception?, messageRes: Int? = null) {
        exception?.printStackTrace()
        messageRes?.let { res ->
            showToast(res)
        }
    }

    fun handleFailure(throwable: Throwable?, messageRes: Int? = null) {
        throwable?.printStackTrace()
        messageRes?.let { res ->
            showToast(res)
        }
    }

    fun changeStatusBarColor(colorRes : Int) {
        activity?.window?.statusBarColor = resources.getColor(colorRes)
    }
}