package com.reephub.praeter.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reephub.praeter.data.IRepository
import com.reephub.praeter.data.local.model.User
import com.reephub.praeter.ui.mainactivity.MainActivityViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: IRepository
) : ViewModel() {

    private val login: MutableLiveData<String> = MutableLiveData()

    ///////////////
    //
    // Observers
    //
    ///////////////
    fun getLogin(): LiveData<String> = login


    ///////////////
    //
    // Functions
    //
    ///////////////
    fun makeCallLogin(user: User) {
        Timber.d("makeCallLogin()")
        viewModelScope.launch(MainActivityViewModel.ioContext) {
            try {
                supervisorScope {
                    val response = repository.login(user)
                    Timber.d("$response")

                    when (response.code) {
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
                    }

                }
            } catch (e: Exception) {
                e.printStackTrace()
                Timber.e(e.message)
            }
        }

    }
}