package com.reephub.praeter.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reephub.praeter.data.IRepository
import com.reephub.praeter.data.remote.dto.LoginResponse
import com.reephub.praeter.data.remote.dto.UserDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: IRepository
) : ViewModel() {

    private val login: MutableLiveData<LoginResponse> = MutableLiveData()

    ///////////////
    //
    // Observers
    //
    ///////////////
    fun getLogin(): LiveData<LoginResponse> = login


    ///////////////
    //
    // Functions
    //
    ///////////////
    fun makeCallLogin(user: UserDto) {
        Timber.d("makeCallLogin()")
        viewModelScope.launch(ioContext) {
            try {
                supervisorScope {
                    val response = repository.login(user)
                    Timber.d("$response")

                    withContext(mainContext) {
                        login.value = response
                    }
                    /*when (response?.code) {
                        200 -> {
                            Timber.e("OK")
                            withContext(MainActivityViewModel.mainContext) {
                                login.value = response.message
                            }
                        }
                        404 -> {
                            Timber.e("Not Found")
                            withContext(MainActivityViewModel.mainContext) {
                                login.value = response.message
                            }
                        }
                        else -> {
                            Timber.e("else")
                            withContext(MainActivityViewModel.mainContext) {
                                login.value = "else"
                            }
                        }
                    }*/

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