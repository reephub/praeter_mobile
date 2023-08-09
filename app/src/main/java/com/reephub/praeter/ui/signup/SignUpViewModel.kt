package com.reephub.praeter.ui.signup

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.reephub.praeter.data.IRepository
import com.reephub.praeter.data.local.model.Screen
import com.reephub.praeter.data.remote.dto.UserDto
import com.reephub.praeter.data.remote.dto.UserResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(private val repository: IRepository) : ViewModel() {

    val routesIndexed: List<Screen> = listOf(
        Screen.Terms,
        Screen.UserForm,
        Screen.Plan,
        Screen.Premium,
        Screen.SuccessfulSignUp
    )

    fun findRoute(navController: NavHostController): Screen? {
        Timber.d("findRoute() | ${navController.currentDestination?.route}")
        return routesIndexed.find { it.route == navController.currentDestination?.route }
    }

    fun getIndexForRoute(route: String): Int = Screen
        .findByRoute(route)
        .run {
            if (Screen.UNKOWN == this) {
                -1
            } else
                routesIndexed.indexOf(this)
        }


    var currentRoute: Screen by mutableStateOf(Screen.Terms)
        private set

    var isTermsChecked: Boolean by mutableStateOf(false)
        private set
    var lastname: String by mutableStateOf("")
        private set
    var firstname: String by mutableStateOf("")
        private set
    var email: String by mutableStateOf("")
        private set
    var password: String by mutableStateOf("")
        private set
    var confirmedPassword: String by mutableStateOf("")
        private set
    var phoneNumber: String by mutableStateOf("")
        private set
    var dateOfBirth: String by mutableStateOf("")
        private set
    var isPremiumPlanSelected: Boolean by mutableStateOf(false)
        private set

    fun updateCurrentRoute(newScreen: Screen) {
        this.currentRoute = newScreen
    }

    fun updateIsTermsChecked(checked: Boolean) {
        this.isTermsChecked = checked
    }

    fun updateLastname(value: String) {
        this.lastname = value
    }

    fun updateFirstname(value: String) {
        this.firstname = value
    }

    fun updateEmail(value: String) {
        this.email = value
    }

    fun updatePassword(value: String) {
        this.password = value
    }

    fun updateConfirmedPassword(value: String) {
        this.confirmedPassword = value
    }

    fun updatePhoneNumber(value: String) {
        this.phoneNumber = value
    }

    fun updateDateOfBirth(value: String) {
        this.dateOfBirth = value
    }

    fun updateIsPremiumPLanSelected(isPremium: Boolean) {
        this.isPremiumPlanSelected = isPremium
    }

    private val shouldShowHideLoading: MutableLiveData<Boolean> = MutableLiveData()
    private val shouldEnableDisableUI: MutableLiveData<Boolean> = MutableLiveData()
    private val shouldShowSuccessCreditCard: MutableLiveData<Boolean> = MutableLiveData()
    private val shouldShowFailedCreditCard: MutableLiveData<Boolean> = MutableLiveData()

    private val saveUserSuccessful: MutableLiveData<UserResponse> = MutableLiveData()
    private val saveUserError: MutableLiveData<UserResponse> = MutableLiveData()

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
    fun getSaveUserSuccessful(): LiveData<UserResponse> = saveUserSuccessful
    fun getSaveUserError(): LiveData<UserResponse> = saveUserError


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

    // SignUp
    fun saveUser() {
        Timber.d("saveUser()")

        // TODO : Remove when done
        currentUser.isCustomer = true
        currentUser.isProvider = false

        viewModelScope.launch(IO) {
            try {
                supervisorScope {
                    val saveResponse = repository.saveUser(currentUser)
                    Timber.d("$saveResponse")

                    // Simulate long-time running operation
                    delay(3000)

                    withContext(Main) {
                        if (401 == saveResponse.code) {
                            saveUserError.value = saveResponse
                        }
                        if (201 == saveResponse.code) {
                            saveUserSuccessful.value = saveResponse
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