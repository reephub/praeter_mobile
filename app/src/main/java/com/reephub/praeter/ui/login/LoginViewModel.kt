package com.reephub.praeter.ui.login

import android.content.Context
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reephub.praeter.BuildConfig
import com.reephub.praeter.core.utils.PraeterNetworkManagerNewAPI
import com.reephub.praeter.data.local.model.LoginUiState
import com.reephub.praeter.data.remote.dto.LoginResponse
import com.reephub.praeter.data.remote.dto.UserDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
//    private val repository: IRepository
) : ViewModel() {
    /////////////////////////////////////
    //
    // Variables
    //
    /////////////////////////////////////


    /////////////////////////////////////
    //
    // Composable states
    //
    /////////////////////////////////////
    private var _loginUiState = MutableStateFlow<LoginUiState>(LoginUiState.None)
    val loginUiState: StateFlow<LoginUiState> = _loginUiState
    var username: String by mutableStateOf(if (BuildConfig.DEBUG) "janedoe@test.fr" else "")
        private set
    var password: String by mutableStateOf(if (BuildConfig.DEBUG) "test" else "")
        private set

    var isPasswordVisible by mutableStateOf(false)
        private set

    var isFormVisible by mutableStateOf(false)
        private set

    var isLoadingVisible by mutableStateOf(false)
        private set

    val isUserNameValid: Boolean by derivedStateOf {
        username.isNotBlank() == true
    }

    fun updateLoginUiState(newState: LoginUiState) {
        _loginUiState.value = newState
    }


    fun updateUsername(username: String) {
        this.username = username
    }

     fun updatePassword(password: String) {
        this.password = password
    }

    fun updateIsFormVisible(show: Boolean) {
        this.isFormVisible = show
    }
    fun updateIsPasswordVisible(show: Boolean) {
        this.isPasswordVisible = show
    }

    fun showLoading() {
        this.isLoadingVisible = true
    }

    fun hideLoading() {
        this.isLoadingVisible = true
    }


    private val login: MutableLiveData<LoginResponse> = MutableLiveData()

    ///////////////
    //
    // Observers
    //
    ///////////////
    fun getLogin(): LiveData<LoginResponse> = login


    ///////////////////////////
    //
    // CLASS METHODS
    //
    ///////////////////////////
    fun checkConnectivity(context: Context) {
        Timber.d("checkConnectivity()")

        PraeterNetworkManagerNewAPI.getInstance(context).apply {
            val isOnline = this.isOnline()
            Timber.d("Is app online : $isOnline")
        }
    }

    /**
     * logging in user. Will make http post request with name, email
     * as parameters
     */
    fun login() {
        Timber.d("login()")

        if (!validateEmail()) {
            return
        }
        if (!validatePassword()) {
            return
        }

        Timber.d("make rest call login")

        showLoading()

        username.let { userLogin ->
            password.let { userPassword ->
                makeCallLogin(
                    UserDto(
                        userLogin,
                        LoginUtils.encodedHashedPassword(LoginUtils.convertToSHA1(userPassword)!!)!!
                    )
                )
            }
        }
    }

    private fun validateEmail() = username.isNotBlank()
    private fun validatePassword() = password.trim().isNotBlank()
    fun makeCallLogin(user: UserDto) {
        Timber.d("makeCallLogin()")
        viewModelScope.launch(IO + SupervisorJob()) {
            try {
                updateLoginUiState(LoginUiState.Connecting)
                delay(2000L)
                updateLoginUiState(LoginUiState.Success("Fake log done "))
            } catch (e: Exception) {
                e.printStackTrace()
                Timber.e(e.message)
            }
        }
    }
}