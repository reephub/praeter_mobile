package com.reephub.praeter.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reephub.praeter.data.IRepository
import com.reephub.praeter.data.remote.dto.LoginResponse
import com.reephub.praeter.data.remote.dto.UserDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext
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
        viewModelScope.launch(IO) {
            try {
                supervisorScope {
                    val response = repository.login(user)
                    Timber.d("$response")

                    withContext(Main) {
                        login.value = response
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Timber.e(e.message)
            }
        }
    }
}