package com.reephub.praeter.ui.mainactivity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reephub.praeter.data.IRepository
import com.reephub.praeter.data.remote.dto.OrderItemDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val repository: IRepository
) : ViewModel() {

    private val orders: MutableLiveData<List<OrderItemDto>> = MutableLiveData()

    ///////////////
    //
    // Observers
    //
    ///////////////
    fun getOrders(): LiveData<List<OrderItemDto>> = orders


    ///////////////
    //
    // Functions
    //
    ///////////////
    fun fetchOrders() {
        Timber.d("fetchOrders()")
        viewModelScope.launch(ioContext) {
            try {
                supervisorScope {
                    val response = repository.getOrders()
                    Timber.d("$response")

                    withContext(mainContext) {
                        orders.value = response
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Timber.e(e.message)
            }
        }
    }

    companion object {
        val ioContext = Dispatchers.IO + Job()
        val mainContext = Dispatchers.Main + Job()
    }
}