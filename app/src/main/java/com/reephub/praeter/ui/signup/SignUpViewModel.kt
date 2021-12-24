package com.reephub.praeter.ui.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reephub.praeter.data.IRepository
import com.reephub.praeter.data.remote.dto.UserDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val repository: IRepository
) : ViewModel() {

    private val shouldShowHideLoading: MutableLiveData<Boolean> = MutableLiveData()
    private val shouldEnableDisableUI: MutableLiveData<Boolean> = MutableLiveData()
    private val shouldShowSuccessCreditCard: MutableLiveData<Boolean> = MutableLiveData()
    private val shouldShowFailedCreditCard: MutableLiveData<Boolean> = MutableLiveData()

    private lateinit var currentUser: UserDto

    /////////////////////////////////////
    //
    // OBSERVERS
    //
    /////////////////////////////////////
    fun getShowHideLoading(): LiveData<Boolean> = shouldShowHideLoading
    fun getEnabledDisableUI(): LiveData<Boolean> = shouldEnableDisableUI
    fun getSuccessCreditCard(): LiveData<Boolean> = shouldShowSuccessCreditCard
    fun getFailedCreditCard(): LiveData<Boolean> = shouldShowFailedCreditCard


    /////////////////////////////////////
    //
    // CLASSES METHODS
    //
    /////////////////////////////////////
    // Form
    fun setFormUser(
        gender: String,
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        phoneNumber: String,
        dateOfBirth: String
    ) {
        currentUser =
            UserDto(gender, firstName, lastName, email, password, phoneNumber, dateOfBirth)
    }

    // Plan
    fun setUserPremium(isPremium: Boolean) {
        Timber.d("setUserPremium()")
        currentUser.isPremium = isPremium
    }

    // Premium
    fun checkCardValidity() {
        Timber.d("checkCardValidity()")

        shouldShowHideLoading.value = true
        shouldEnableDisableUI.value = false

        viewModelScope.launch {

            delay(TimeUnit.SECONDS.toMillis(2))

            shouldShowHideLoading.value = false
            shouldEnableDisableUI.value = true

            // TODO : set on error to the view
            shouldShowSuccessCreditCard.value = true

        }
    }
}