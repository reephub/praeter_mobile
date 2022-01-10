package com.reephub.praeter.ui.home

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reephub.praeter.data.IRepository
import com.reephub.praeter.data.remote.dto.directions.GoogleDirectionsResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import timber.log.Timber
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HomeLocationViewModel @Inject constructor(
    private val repository: IRepository
) : ViewModel() {

    private val googleDirection: MutableLiveData<GoogleDirectionsResponse> = MutableLiveData()
    private val address: MutableLiveData<Address> = MutableLiveData()

    fun getGoogleDirection(): LiveData<GoogleDirectionsResponse> {
        return googleDirection
    }

    fun getAddress(): LiveData<Address> {
        return address
    }

    fun getItinerary(origin: String, destination: String) {
        Timber.d("getItinerary()")

        viewModelScope.launch(ioContext) {
            try {
                supervisorScope {
                    val route = repository.getDirections(origin, destination)

                    Timber.d("$route")

                    withContext(mainContext) {
                        googleDirection.value = route
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Timber.e(e.message)
            }

        }
    }

    fun getAddressFromLocation(context: Context, location: Location) {
        Timber.d("getAddressFromLocation()")

        val addresses: List<Address>
        val geocoder = Geocoder(context, Locale.getDefault())

        addresses = geocoder.getFromLocation(
            location.latitude,
            location.longitude,
            1
        ) // Here 1 represent max location result to returned, by documents it recommended 1 to 5

        address.value = addresses[0]
    }

    companion object {
        val ioContext = Dispatchers.IO + Job()
        val mainContext = Dispatchers.Main + Job()
    }
}