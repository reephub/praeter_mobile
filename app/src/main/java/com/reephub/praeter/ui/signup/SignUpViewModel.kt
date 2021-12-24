package com.reephub.praeter.ui.signup

import androidx.lifecycle.ViewModel
import com.reephub.praeter.data.IRepository
import com.reephub.praeter.data.remote.dto.UserDto
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val repository: IRepository
) : ViewModel() {

    private val currentUser: UserDto = UserDto()


}