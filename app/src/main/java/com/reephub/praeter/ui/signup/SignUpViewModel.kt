package com.reephub.praeter.ui.signup

import android.content.Context
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.reephub.praeter.R
import com.reephub.praeter.data.IRepository
import com.reephub.praeter.data.local.model.Screen
import com.reephub.praeter.data.remote.dto.UserDto
import com.reephub.praeter.data.remote.dto.UserResponse
import com.reephub.praeter.utils.isValidEmail
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
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
class SignUpViewModel @Inject constructor(
    @ApplicationContext private val application: Context,
    private val repository: IRepository
) : ViewModel() {

    val routesIndexed: List<Pair<Screen, Int>> = listOf(
        Pair(Screen.Terms, 0),
        Pair(Screen.UserForm, 1),
        Pair(Screen.Plan, 2),
        Pair(Screen.Premium, 3),
        Pair(Screen.SuccessfulSignUp, 4)
    )

    fun findRoute(navController: NavHostController): Screen? {
        Timber.d("findRoute() | ${navController.currentDestination?.route}")
        return routesIndexed.find { it.first.route == navController.currentDestination?.route }?.first
    }

    fun getIndexForRoute(route: String): Int = Screen
        .findByRoute(route)
        .run {
            if (Screen.UNKOWN == this) {
                -1
            } else {
                updateCurrentIndex(routesIndexed.indexOfFirst { it.first == this })
                routesIndexed.indexOfFirst { it.first == this }
            }
        }


    var currentRoute: Screen by mutableStateOf(Screen.Terms)
        private set
    var currentIndex: Int by mutableStateOf(0)
        private set

    var isTermsChecked: Boolean by mutableStateOf(false)
        private set


    var isGenderExpanded: Boolean by mutableStateOf(false)
        private set

    var genderOptions = listOf("M.", "Mme")
    var gender: String by mutableStateOf("")
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

    val formCompleted: Boolean by derivedStateOf {
        gender.isNotBlank()
                && firstname.isNotBlank()
                && lastname.isNotBlank()
                && email.isNotBlank()
                && password.isNotBlank()
                && confirmedPassword.isNotBlank()
                && phoneNumber.isNotBlank()
    }

    var isPremiumPlanSelected: Boolean by mutableStateOf(false)
        private set

    var shouldShowToast: String by mutableStateOf("")
        private set


    fun updateShowToast(message: String) {
        this.shouldShowToast = message
    }

    fun updateCurrentRoute(newScreen: Screen) {
        this.currentRoute = newScreen
    }

    fun updateCurrentIndex(newIndex: Int) {
        this.currentIndex = newIndex
    }

    fun updateIsTermsChecked(checked: Boolean) {
        this.isTermsChecked = checked
    }

    fun updateIsGenderExpanded(expanded: Boolean) {
        this.isGenderExpanded = expanded
    }


    fun updateGender(value: String) {
        this.gender = value
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

    init {
        preloadData()
    }


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
    /////////////////////////////////////.
    private fun preloadData() {
        updateLastname("Doe")
        updateFirstname("John")
        updateEmail("john.doe@test.fr")
        updatePassword("johndoe")
        updateConfirmedPassword("johndoe")
        updatePhoneNumber("06123456789")
    }

    // Form
    /**
     * Validating form
     */
    fun submitForm(): Boolean {
        if (!validateGender()) {
            return false
        }
        if (!validateLastName()) {
            return false
        }
        if (!validateFirstName()) {
            return false
        }
        if (!validateEmail()) {
            return false
        }
        if (!validatePassword()) {
            return false
        }
        if (!validateConfirmPassword()) {
            return false
        }
        if (!validatePhone()) {
            return false
        }
        if (!validateDateOfBirth()) {
            return false
        }

        setFormUser(gender, firstname, lastname, email, password, phoneNumber, dateOfBirth)

        updateShowToast("Thank You!")
        return true
    }

    private fun validateGender(): Boolean {
        if (gender.isBlank()) {
            updateShowToast("Please select a gender")
            return false
        }
        return true
    }

    private fun validateLastName(): Boolean {
        if (lastname.trim().isEmpty()) {
            updateShowToast(application.getString(R.string.err_msg_form_last_name))
            // requestFocus(binding.inputLastName)
            return false
        } else {
            // binding.inputLayoutLastName.isErrorEnabled = false
        }
        return true
    }


    private fun validateFirstName(): Boolean {
        if (firstname.trim().isEmpty()) {
            updateShowToast(application.getString(R.string.err_msg_form_first_name))
            // requestFocus(binding.inputFirstName)
            return false
        } else {
            // binding.inputLayoutFirstName.isErrorEnabled = false
        }
        return true
    }

    private fun validateEmail(): Boolean {
        if (email.trim().isEmpty() || !email.isValidEmail()) {
            updateShowToast(application.getString(R.string.err_msg_form_email))
            // requestFocus(binding.inputEmail)
            return false
        } else {
            // binding.inputLayoutEmail.isErrorEnabled = false
        }
        return true
    }

    private fun validatePassword(): Boolean {
        if (password.trim().isEmpty()) {
            updateShowToast(application.getString(R.string.err_msg_form_password))
            //requestFocus(binding.inputPassword)
            return false
        } else {
            // binding.inputLayoutPassword.isErrorEnabled = false
        }
        return true
    }


    private fun validateConfirmPassword(): Boolean {
        if (confirmedPassword.trim().isEmpty()
            && confirmedPassword.trim() != password.trim().toString()
        ) {
            updateShowToast(application.getString(R.string.err_msg_form_confirm_password))
            //  requestFocus(binding.inputConfirmPassword)
            return false
        } else {
            //binding.inputLayoutConfirmPassword.isErrorEnabled = false
        }
        return true
    }


    private fun validatePhone(): Boolean {
        if (phoneNumber.trim().isEmpty()) {
            updateShowToast(application.getString(R.string.err_msg_form_phone_number))
            // requestFocus(binding.inputPhoneNumber)
            return false
        } else {
            //  binding.inputLayoutPhoneNumber.isErrorEnabled = false
        }
        return true
    }


    private fun validateDateOfBirth(): Boolean {
        /*if (inputDateOfBirth.text.toString().trim().isEmpty()) {
            inputLayoutDateOfBirth.setError(context.getString(R.string.err_msg_form_date_of_birth));
            requestFocus(inputDateOfBirth);
            return false;
        } else {
            inputLayoutDateOfBirth.setErrorEnabled(false);
        }
*/
        return true
    }

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