package com.reephub.praeter.ui.mainactivity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reephub.praeter.data.IRepository
import com.reephub.praeter.data.remote.dto.AncientDto
import com.reephub.praeter.data.remote.dto.ClassesDto
import com.reephub.praeter.data.remote.dto.OrderItemDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val repository: IRepository
) : ViewModel() {

    private val classes: MutableLiveData<List<ClassesDto>> = MutableLiveData()
    private val ancients: MutableLiveData<List<AncientDto>> = MutableLiveData()
    private val orders: MutableLiveData<List<OrderItemDto>> = MutableLiveData()

    ///////////////
    //
    // Observers
    //
    ///////////////
    fun getOrders(): LiveData<List<OrderItemDto>> = orders
    fun getClasses(): LiveData<List<ClassesDto>> = classes
    fun getAncients(): LiveData<List<AncientDto>> = ancients


    ///////////////
    //
    // Functions
    //
    ///////////////
    fun fetchClasses() {
        Timber.d("fetchClasses()")
        viewModelScope.launch(ioContext) {
            try {
                supervisorScope {
                    val response = repository.getClasses()
                    Timber.d("$response")

                    withContext(mainContext) {
                        classes.value = response
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Timber.e(e.message)
            }
        }
    }

    fun fetchAncients() {
        Timber.d("fetchAncients()")
        viewModelScope.launch(ioContext) {
            try {
                supervisorScope {
                    val response = repository.getAncients()
                    Timber.d("$response")

                    withContext(mainContext) {
                        ancients.value = response
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Timber.e(e.message)
            }
        }
    }

    fun fetchOrders() {
        Timber.d("fetchOrders()")
        viewModelScope.launch(ioContext) {
            try {
                supervisorScope {
                    val response = repository.getOrders()
                    Timber.d("$response")

                    val list = mutableListOf<OrderItemDto>()

                    response.forEach { orderDto -> list.addAll(orderDto.contents) }

                    withContext(mainContext) {
                        orders.value = list
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