package com.reephub.praeter.ui.mainactivity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reephub.praeter.data.IRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val repository: IRepository
) : ViewModel() {

    fun fetchOrders() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                supervisorScope {
                    val response = repository.getOrders()
                    Timber.d("$response")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}