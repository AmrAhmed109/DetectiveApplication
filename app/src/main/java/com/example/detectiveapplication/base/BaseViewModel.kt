package com.englizya.common.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
open class BaseViewModel @Inject constructor()  : ViewModel() {

     val TAG = this::class.java.name

    private val _loading: MutableLiveData<Boolean> = MutableLiveData()
    val loading: LiveData<Boolean> = _loading

    private val _error: MutableLiveData<Exception?> = MutableLiveData()
    val error: LiveData<Exception?> = _error

    private val _connectivity: MutableLiveData<Boolean> = MutableLiveData(false)
    val connectivity: LiveData<Boolean> = _connectivity

    fun handleException(exception: Exception?) {
        //TODO check for every exception type print specific message
        exception?.printStackTrace()
    }

    fun updateLoading(state: Boolean) {
        _loading.postValue(state)
    }

    fun handleException(exception: Throwable?) {
        //TODO check for every exception type print specific message
        exception?.printStackTrace()
    }

    fun updateConnectivity(connectivity: Boolean) {
        _connectivity.postValue(connectivity)
    }
}